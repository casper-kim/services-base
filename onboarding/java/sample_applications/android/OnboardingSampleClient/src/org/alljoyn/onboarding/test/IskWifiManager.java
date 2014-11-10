/******************************************************************************
 *    Copyright (c) Open Connectivity Foundation (OCF) and AllJoyn Open
 *    Source Project (AJOSP) Contributors and others.
 *
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    All rights reserved. This program and the accompanying materials are
 *    made available under the terms of the Apache License, Version 2.0
 *    which accompanies this distribution, and is available at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Copyright (c) Open Connectivity Foundation and Contributors to AllSeen
 *    Alliance. All rights reserved.
 *
 *    Permission to use, copy, modify, and/or distribute this software for
 *    any purpose with or without fee is hereby granted, provided that the
 *    above copyright notice and this permission notice appear in all
 *    copies.
 *
 *     THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL
 *     WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED
 *     WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE
 *     AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL
 *     DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR
 *     PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER
 *     TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 *     PERFORMANCE OF THIS SOFTWARE.
 ******************************************************************************/

package org.alljoyn.onboarding.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.alljoyn.onboarding.OnboardingService.AuthType;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.util.Pair;

/**
 *  
 */
public class IskWifiManager 
{
	
	final String INT_ENTERPRISEFIELD_NAME ="android.net.wifi.WifiConfiguration$EnterpriseField";
	private static final String TAG = "IskWifiManager";
	// Stores the details of the target Wi-Fi. Uses volatile to verify that the
	// broadcast receiver reads its content each time onReceive is called, thus
	// "knowing" if the an API call to connect has been made
	private volatile WifiConfiguration targetWifiConfiguration = null;
	
    // Timer for checking completion of Wi-Fi tasks.
    private Timer wifiTimeoutTimer = new Timer();
	//WEP HEX password pattern.
    static final String WEP_HEX_PATTERN = "[\\dA-Fa-f]+";
	private Context m_context;
	//  Android Wi-Fi manager
	private WifiManager m_wifi;
	// Listener for events generated by this class
	private WifiManagerListener m_listener;
	// Receiver for Wi-Fi intents
	private BroadcastReceiver m_scanner;
	

	//================================================================
	public IskWifiManager(Context c) {
		m_context = c;
		m_wifi = (WifiManager) m_context.getSystemService(Context.WIFI_SERVICE);		
	}
	//================================================================
	/**
	 * Kick off a Wi-Fi scan, after registering for Wi-Fi intents
	 * @param c context
	 * @param listener listener to events generated by this class
	 * @param AJlookupPrefix filter for discovered devices
	 */
	public void scanForWifi(Context c, WifiManagerListener listener, String AJlookupPrefix)
	{
		m_listener = listener;
		m_context = c;
		m_wifi = (WifiManager) m_context.getSystemService(Context.WIFI_SERVICE);

		// listen to Wi-Fi intents
		m_scanner = new BroadcastReceiver() {

			// will get here after scan
			@Override
			public void onReceive(Context context, Intent intent) 
			{
				// === Current scans ===
				List<ScanResult> scans = m_wifi.getScanResults();
				
				// remove duplicate SSID with different BSSID ,
				if (scans != null){
					
					// keep one item per SSID, the one with the strongest signal
					HashMap<String, ScanResult> alljoynSoftAPs = new HashMap<String, ScanResult>(); 
					for (ScanResult currentScan : scans){
						ScanResult l=alljoynSoftAPs.get(currentScan.SSID);
						if (l==null)
						{
							alljoynSoftAPs.put(currentScan.SSID, currentScan);
						}else{
							if (l.level<currentScan.level)
							{
								alljoynSoftAPs.put(currentScan.SSID, currentScan);
							}
						}

					}
					
					// sort list by level of Wi-Fi signal
					List <ScanResult> list=new ArrayList<ScanResult>(alljoynSoftAPs.values());
					Collections.sort(list, new Comparator<ScanResult>() {
						public int compare(ScanResult o1, ScanResult o2) {
							if ( o1.level>o2.level)
								return -1;
							else if ( o1.level<o2.level)
								return 1;
							else 
								return 0;
						}
					});
					// listener callback
					m_listener.OnScanResultComplete(list);

				}
			}
		};
		
		// register for Wi-Fi intents that will be generated by the scanning process
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		filter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
		filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		m_context.registerReceiver(m_scanner, filter);
		
		// start a scan
		m_wifi.startScan();
		
	}
	
	//================================================================
	public void unregisterWifiManager(){
		if(m_scanner != null){
    		try{
    			if(m_context != null)
    				m_context.unregisterReceiver(m_scanner);
    		} catch (Exception e) {
			}
    	}
	}
	//================================================================
	
	/**
	 * Connect to a a Wi-Fi Access Point
	 * @param networkName SSID of the network
	 * @param passkey Wi-Fi password
	 * @param capabilities the Wi-Fi capabilities string: supported authentication
	 * @param isPasswordASCII indicates whether the password is in ASCII format or HEX.
	 * @return
	 */
	public boolean connectToAP(String networkName, String passkey, String capabilities) 
	{
	    Log.i(TAG, "* connectToAP with capabilities");
	    AuthType securityMode = getScanResultSecurity(capabilities);
	    return connectToAP(networkName,passkey, securityMode.getTypeId());
	}
	//================================================================
	
	/**
	 * Map a capabilities string from Android's scan to AllJoyn Onboarding service AuthType enum
	 * @param capabilities the AP's capabilities screen. Authentication
	 * @return AuthType
	 * @see AuthType
	 */
	public AuthType getScanResultSecurity(String capabilities) {
	    Log.i(TAG, "* getScanResultSecurity");

	    if (capabilities.contains(AuthType.WEP.name())) {
	    	return AuthType.WEP;
	    }
	    else if (capabilities.contains("WPA")) {
	    	if (capabilities.contains("WPA2"))
		    	return AuthType.WPA2_AUTO;
	    	else
	    		return AuthType.WPA_AUTO;
	    }	       
		return AuthType.OPEN;

	}
	
	//================================================================
	/**
	 * @return the Wi-Fi AP that we're logged into
	 */
	public String getCurrentNetworkSSID(){
		
		String s = m_wifi.getConnectionInfo().getSSID();
		return s;
	}
	
	/**
	 * @return compare result of two SSID strings.
	 */
	public static boolean isSsidEquals(String ssid1, String ssid2)
	{
		if (ssid1 == null || ssid1.length() == 0 || ssid2 == null || ssid2.length() == 0)
			return false;
		if (ssid1.startsWith("\"")){
			ssid1 = ssid1.replace("\"", "");
		}
		if (ssid2.startsWith("\"")){
			ssid2 = ssid2.replace("\"", "");
		}
		return ssid1.equals(ssid2);
	}
	
	
	//================================================================
	/**
	 * Connect to an AJ Wi-Fi Access Point
	 * @param isPasswordASCII indicates whether the password is in ASCII format or HEX.
	 * @param networkName SSID of the network
	 * @param passkey Wi-Fi password
	 * @param authType AuthType 
	 */
	public boolean connectToAP(String ssid, String password, short authTypeCode) {

		AuthType authType = AuthType.getAuthTypeById(authTypeCode);
		Log.d(TAG, "connectToAP SSID = " + ssid + " authtype = " + authType.toString());

        // if networkPass is null set it to ""
        if (password == null) {
            password = "";
        }

        // the configured Wi-Fi networks
        final List<WifiConfiguration> wifiConfigs = m_wifi.getConfiguredNetworks();

        // log the list
        StringBuffer buff = new StringBuffer();
        for (WifiConfiguration w : wifiConfigs) {
            if (w.SSID != null) {
                w.SSID = normalizeSSID(w.SSID);
                if (w.SSID.length() > 1) {
                    buff.append(w.SSID).append(",");
                }
            }
        }
        Log.i(TAG, "connectToWifiAP ConfiguredNetworks " + (buff.length() > 0 ? buff.toString().substring(0, buff.length() - 1) : " empty"));

        int networkId = -1;

        // delete any existing WifiConfiguration that has the same SSID as the
        // new one
        for (WifiConfiguration w : wifiConfigs) {
            if (w.SSID != null && isSsidEquals(w.SSID, ssid)) {
                networkId = w.networkId;
                Log.i(TAG, "connectToWifiAP found " + ssid + " in ConfiguredNetworks. networkId = " + networkId);
                boolean res = m_wifi.removeNetwork(networkId);
                Log.i(TAG, "connectToWifiAP delete  " + networkId + "? " + res);
                res = m_wifi.saveConfiguration();
                Log.i(TAG, "connectToWifiAP saveConfiguration  res = " + res);
                break;
            }
        }

        WifiConfiguration wifiConfiguration = new WifiConfiguration();

        // check the AuthType of the SSID against the WifiManager
        // if null use the one given by the API
        // else use the result from getSSIDAuthType
        AuthType verrifiedWifiAuthType = getSSIDAuthType(ssid);
        if (verrifiedWifiAuthType != null) {
        	authType = verrifiedWifiAuthType;
        }

        Log.i(TAG, "connectToWifiAP selectedAuthType = " + authType);

        // set the WifiConfiguration parameters
        switch (authType) {
        case OPEN:
            wifiConfiguration.SSID = "\"" + ssid + "\"";
            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            networkId = m_wifi.addNetwork(wifiConfiguration);
            Log.d(TAG, "connectToWifiAP [OPEN] add Network returned " + networkId);
            break;

        case WEP:

        	wifiConfiguration.SSID = "\"" + ssid + "\"";
            // check the validity of a WEP password
            Pair<Boolean, Boolean> wepCheckResult = checkWEPPassword(password);
            if (!wepCheckResult.first) {
                Log.i(TAG, "connectToWifiAP  auth type = WEP: password " + password + " invalid length or charecters");
                return false;
            }
            Log.i(TAG, "connectToWifiAP [WEP] using " + (!wepCheckResult.second ? "ASCII" : "HEX"));
            if (!wepCheckResult.second) {
                wifiConfiguration.wepKeys[0] = "\"" + password + "\"";
            } else {
                wifiConfiguration.wepKeys[0] = password;
            }
            wifiConfiguration.priority = 40;
            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            wifiConfiguration.wepTxKeyIndex = 0;
            networkId = m_wifi.addNetwork(wifiConfiguration);
            Log.d(TAG, "connectToWifiAP [WEP] add Network returned " + networkId);
            break;

        case WPA_AUTO:
        case WPA_CCMP:
        case WPA_TKIP:
        case WPA2_AUTO:
        case WPA2_CCMP:
        case WPA2_TKIP: {
            wifiConfiguration.SSID = "\"" + ssid + "\"";
            // handle special case when WPA/WPA2 and 64 length password that can
            // be HEX
            if (password.length() == 64 && password.matches(WEP_HEX_PATTERN)) {
                wifiConfiguration.preSharedKey = password;
            } else {
                wifiConfiguration.preSharedKey = "\"" + password + "\"";
            }
            wifiConfiguration.hiddenSSID = true;
            wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            networkId = m_wifi.addNetwork(wifiConfiguration);
            Log.d(TAG, "connectToWifiAP  [WPA..WPA2] add Network returned " + networkId);
            break;
        }
        default:
            networkId = -1;
            break;
        }
        if (networkId < 0) {
            Log.d(TAG, "connectToWifiAP networkId <0  WIFI_AUTHENTICATION_ERROR");
            return false;
        }
        Log.d(TAG, "connectToWifiAP calling connect");
        connect(wifiConfiguration, networkId, 30*1000);
        return true;
	}
	
	//======================================================================
	/**
     * Make the actual connection to the requested Wi-Fi target.
     *
     * @param wifiConfig
     *            details of the Wi-Fi access point used by the WifiManger
     * @param networkId
     *            id of the Wi-Fi configuration
     * @param timeoutMsec
     *            period of time in Msec to complete Wi-Fi connection task
     */
    private void connect(final WifiConfiguration wifiConfig, final int networkId, final long timeoutMsec) {
       
    	Log.i(TAG, "connect  SSID=" + wifiConfig.SSID + " within " + timeoutMsec);
        boolean res;

        synchronized (this) {
            targetWifiConfiguration = wifiConfig;
        }

        // this is the application's Wi-Fi connection timeout
        wifiTimeoutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e(TAG, "Network Listener WIFI_TIMEOUT  when trying to connect to " + normalizeSSID(targetWifiConfiguration.SSID));
            }
        }, timeoutMsec);

        if (m_wifi.getConnectionInfo().getSupplicantState() == SupplicantState.DISCONNECTED) {
        	m_wifi.disconnect();
        }

        res = m_wifi.enableNetwork(networkId, false);
        Log.d(TAG, "connect enableNetwork [false] status=" + res);
        res = m_wifi.disconnect();
        Log.d(TAG, "connect disconnect  status=" + res);

        // enabling a network doesn't guarantee that it's the one that Android
        // will connect to.
        // Selecting a particular network is achieved by passing 'true' here to
        // disable all other networks.
        // the side effect is that all other user's Wi-Fi networks become
        // disabled.
        // The recovery for that is enableAllWifiNetworks method.
        res = m_wifi.enableNetwork(networkId, true);
        Log.d(TAG, "connect enableNetwork [true] status=" + res);
        res = m_wifi.reconnect();
        m_wifi.setWifiEnabled(true);
    }
    
	//======================================================================
	 private static final char[] HEX_CODE = "0123456789ABCDEF".toCharArray();
    
	 /**
	 * Convert a given password in decimal format to hex format.
	 * @param pass The given password in decimal format.
	 * @return The password in hex format.
	 */
	public String toHexadecimalString(String pass) {
		 
		 Log.d(TAG, "toHexadecimalString("+pass+")");
		 byte[] data;
		 try {
			 data = pass.getBytes("UTF-8");
		 } catch (UnsupportedEncodingException e) {
			 Log.e(TAG, "Failed getting bytes of passcode by UTF-8", e);
			 data = pass.getBytes();
		 }
		 StringBuilder r = new StringBuilder(data.length*2);
		 for ( byte b : data) {
			 r.append(HEX_CODE[(b >> 4) & 0xF]);
			 r.append(HEX_CODE[(b & 0xF)]);
		 }
		 String hexadecimalString = r.toString();
		 Log.d(TAG, "toHexadecimalString -->"+hexadecimalString);
		 return hexadecimalString;
    }
	//======================================================================
	/**
    * A utility method that checks if a password complies with WEP password
    * rules, and if it's in HEX format.
    *
    * @param password
    * @return {@link Pair} of two {@link Boolean} is it a valid WEP password
    *         and is it a HEX password.
    */
   public Pair<Boolean, Boolean> checkWEPPassword(String password) {
       Log.d(TAG, "checkWEPPassword");

       if (password == null || password.isEmpty()) {
           Log.w(TAG, "checkWEPPassword empty password");
           return new Pair<Boolean, Boolean>(false, false);
       }

       int length = password.length();
       switch (length) {
       // valid ASCII keys length
       case 5:
       case 13:
       case 16:
       case 29:
           Log.d(TAG, "checkWEPPassword valid WEP ASCII password");
           return new Pair<Boolean, Boolean>(true, false);
           // valid hex keys length
       case 10:
       case 26:
       case 32:
       case 58:
           if (password.matches(WEP_HEX_PATTERN)) {
               Log.d(TAG, "checkWEPPassword valid WEP password length, and HEX pattern match");
               return new Pair<Boolean, Boolean>(true, true);
           }
           Log.w(TAG, "checkWEPPassword valid WEP password length, but HEX pattern matching failed: " + WEP_HEX_PATTERN);
           return new Pair<Boolean, Boolean>(false, false);
       default:
           Log.w(TAG, "checkWEPPassword invalid WEP password length: " + length);
           return new Pair<Boolean, Boolean>(false, false);
       }
   }
	//====================================================================
   /**
    * A utility method for removing wrapping quotes from SSID name. Some
    * Android devices return an SSID surrounded with quotes. For the sake of
    * comparison and readability, remove those.
    *
    * @param ssid
    *            could be AJ_QA but also "AJ_QA" (with quotes).
    * @return normalized SSID: AJ_QA
    */
   static String normalizeSSID(String ssid) {
       if (ssid != null && ssid.length() > 0 && ssid.startsWith("\"")) {
           ssid = ssid.replace("\"", "");
       }
       return ssid;
   }
   //======================================================================
   /**
    * Extracts AuthType from a SSID by retrieving its capabilities via WifiManager.
    *
    * @param ssid
    * @return AuthType of SSID or null if not found
    */
   private AuthType getSSIDAuthType(String ssid) {
       Log.d(TAG, "getSSIDAuthType SSID = " + ssid);
       if (ssid == null || ssid.length() == 0) {
           Log.w(TAG, "getSSIDAuthType given string was null");
           return null;
       }
       List<ScanResult> networks = m_wifi.getScanResults();
       for (ScanResult scan : networks) {
           if (ssid.equalsIgnoreCase(scan.SSID)) {
               AuthType res = getScanResultSecurity(scan.capabilities);
               return res;
           }
       }
       return null;
   }
   //======================================================================
}