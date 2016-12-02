package org.alljoyn.ioe.controlpanelbrowser;
/******************************************************************************
* * 
*    Copyright (c) 2016 Open Connectivity Foundation and AllJoyn Open
*    Source Project Contributors and others.
*    
*    All rights reserved. This program and the accompanying materials are
*    made available under the terms of the Apache License, Version 2.0
*    which accompanies this distribution, and is available at
*    http://www.apache.org/licenses/LICENSE-2.0

******************************************************************************/

import org.alljoyn.ioe.controlpanelbrowser.DeviceDetailFragment.DeviceDetailCallback;
import org.alljoyn.ioe.controlpanelbrowser.DeviceList.DeviceContext;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * An activity representing a list of Appliances. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link DeviceDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link DeviceListFragment} and the item details (if present) is a
 * {@link DeviceDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link DeviceListFragment.DeviceListCallback} interface to listen for item
 * selections.
 */
public class DeviceListActivity extends FragmentActivity implements DeviceListFragment.DeviceListCallback, DeviceDetailCallback {

    /**
     * For logging
     */
    private final static String TAG = "cpappApplianceListActivity";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean isTwoPane;

    /**
     * The {@link Fragment} containing the Control Panel
     */
    private DeviceDetailFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        if (findViewById(R.id.appliance_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            isTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((DeviceListFragment) getSupportFragmentManager().findFragmentById(R.id.appliance_list)).setActivateOnItemClick(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * Callback method from {@link DeviceListFragment.DeviceListCallback}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(DeviceContext context) {

        if (isTwoPane) {

            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(DeviceDetailFragment.ARG_ITEM_ID, context);

            fragment = new DeviceDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.appliance_detail_container, fragment).commit();

        } else {

            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, DeviceDetailActivity.class);
            detailIntent.putExtra(DeviceDetailFragment.ARG_ITEM_ID, context);
            startActivity(detailIntent);
        }
    }

    /**
     * @see org.alljoyn.ioe.controlpanelbrowser.DeviceDetailFragment.DeviceDetailCallback#onControlPanelStale()
     */
    @Override
    public void onControlPanelStale() {

        if ( fragment == null ) {

            Log.wtf(TAG, "onControlPanelStale was called, but Fragment is NULL, weird...");
            return;
        }

        Log.d(TAG, "onControlPanelStale was called need to detach the DeviceDetailFragment");
        getSupportFragmentManager().beginTransaction().detach(fragment).commit();
    }

}