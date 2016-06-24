/******************************************************************************
 * Copyright AllSeen Alliance. All rights reserved.
 *
 *    Permission to use, copy, modify, and/or distribute this software for any
 *    purpose with or without fee is hereby granted, provided that the above
 *    copyright notice and this permission notice appear in all copies.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 *    WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 *    MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 *    ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 *    WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 *    ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 *    OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ******************************************************************************/
package org.alljoyn.ioe.controlpanelservice.communication;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.alljoyn.bus.AboutKeys;
import org.alljoyn.bus.AboutListener;
import org.alljoyn.bus.AboutObjectDescription;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.Variant;
import org.alljoyn.bus.VariantUtil;
import org.alljoyn.common.TransformUtil;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Receives announcement signals
 */
public class ControlPanelServiceAboutListener implements AboutListener {
    private static final String TAG = "cpan" + ControlPanelServiceAboutListener.class.getSimpleName();

    /**
     * @see org.alljoyn.bus.AboutListener#announced(String, int, short, AboutObjectDescription[], Map)
     */
    @Override
    public void announced(String busName, int version, short port, AboutObjectDescription[] objectDescriptions, Map<String, Variant> aboutData) {
        Log.v(TAG, "Received Announcement signal");
        Handler handler = ConnectionManager.getInstance().getHandler();

        if (handler == null) {
            return;
        }

        UUID appId;
        String deviceId;

        try {
            Variant varAppId = aboutData.get(AboutKeys.ABOUT_APP_ID);
            String appIdSig = VariantUtil.getSignature(varAppId);
            if (!appIdSig.equals("ay")) {
                Log.e(TAG, "Received '" + AboutKeys.ABOUT_APP_ID + "', that has an unexpected signature: '" + appIdSig + "', the expected signature is: 'ay'");
                return;
            }

            byte[] rawAppId = varAppId.getObject(byte[].class);
            appId = TransformUtil.byteArrayToUUID(rawAppId);

            if (appId == null) {
                Log.e(TAG, "Failed to translate the received AppId into UUID");
                return;
            }

            Variant varDeviceId = aboutData.get(AboutKeys.ABOUT_DEVICE_ID);
            String devIdSig = VariantUtil.getSignature(varDeviceId);
            if (!devIdSig.equals("s")) {
                Log.e(TAG, "Received '" + AboutKeys.ABOUT_DEVICE_ID + "', that has an unexpected signature: '" + devIdSig + "', the expected signature is: 's'");
                return;
            }

            deviceId = varDeviceId.getObject(String.class);
        } catch (BusException be) {
            Log.e(TAG, "Failed to retreive an Announcement properties, Error: '" + be.getMessage() + "'");
            return;
        }

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("SENDER", busName);
        args.put("DEVICE_ID", deviceId);
        args.put("APP_ID", appId.toString());
        args.put("OBJ_DESC", objectDescriptions);

        Message.obtain(handler, ConnManagerEventType.ANNOUNCEMENT_RECEIVED.ordinal(), args).sendToTarget();
    }
}
