/******************************************************************************
 *  * 
 *    Copyright (c) 2016 Open Connectivity Foundation and AllJoyn Open
 *    Source Project Contributors and others.
 *    
 *    All rights reserved. This program and the accompanying materials are
 *    made available under the terms of the Apache License, Version 2.0
 *    which accompanies this distribution, and is available at
 *    http://www.apache.org/licenses/LICENSE-2.0

 ******************************************************************************/

package org.allseen.timeservice.sample.server.logic;

import java.util.ArrayList;
import java.util.List;

import org.alljoyn.bus.ErrorReplyBusException;
import org.allseen.timeservice.TimeServiceConst;
import org.allseen.timeservice.sample.server.ui.TimeSampleServer;
import org.allseen.timeservice.server.Alarm;
import org.allseen.timeservice.server.AlarmFactory;

import android.content.Context;
import android.util.Log;

/**
 * Represents an AlarmFactory {@link org.allseen.timeservice.server.AlarmFactory}.
 * Encapsulates access to list of alarms.
 * <ul>
 *      <li> Enables creation of new Alarm  {@link org.allseen.timeservice.server.Alarm}.
 *      <li> Enables delete of an Alarm  {@link org.allseen.timeservice.server.Alarm}.
 *      <li> Enables search for an Alarm {@link org.allseen.timeservice.server.Alarm}.
 * </ul>
 */
public class ServerAlarmFactory extends AlarmFactory {

    /**
     * Log Tag
     */
    private static final String TAG = "ServerAlarmFactory";

    /**
     *
     */
    private final Context context;

    /**
     * Alarms created by this Alarm Factory
     */
    private final List<Alarm> alarms;

    /**
     * Constructor
     */
    public ServerAlarmFactory(Context context) {
        this.context = context;
        alarms = new ArrayList<Alarm>();
        TimeSampleServer.sendMessage(context, TimeSampleServer.ALARM_EVENT_ACTION, getObjectPath(), "AlarmFactory has been created");
    }

    /**
     * @see org.allseen.timeservice.server.AlarmFactory#release()
     */
    @Override
    public void release() {

        for (Alarm alarm : alarms) {

            alarm.release();
        }
        alarms.clear();

        super.release();
        TimeSampleServer.sendMessage(context, TimeSampleServer.ALARM_EVENT_ACTION, getObjectPath(), "AlarmFactory has been released");
    }

    /**
     * @see org.allseen.timeservice.server.AlarmFactory#newAlarm()
     */
    @Override
    public Alarm newAlarm() throws ErrorReplyBusException {

        TimeSampleServer.sendMessage(context, TimeSampleServer.ALARM_EVENT_ACTION, getObjectPath(), "AlarmFactory newAlarm request");
        Alarm alarm = new ServerAlarm(context);
        alarms.add(alarm);

        Log.i(TAG, "Created new Alarm by Factory");
        return alarm;
    }

    /**
     * @see org.allseen.timeservice.server.AlarmFactory#deleteAlarm(java.lang.String)
     */
    @Override
    public void deleteAlarm(String objectPath) throws ErrorReplyBusException {

        TimeSampleServer.sendMessage(context, TimeSampleServer.ALARM_EVENT_ACTION, getObjectPath(), "AlarmFactory deleteAlarm request");
        Alarm alarm = findAlarm(objectPath);

        if (alarm != null) {

            Log.i(TAG, "Removing Alarm, objectPath: '" + objectPath + "'");
            alarm.release();

            alarms.remove(alarm);

            return;
        }

        throw new ErrorReplyBusException(TimeServiceConst.GENERIC_ERROR, "Alarm: '" + objectPath + "' is not found");
    }

    /**
     * Search for an Alarm in the Alarm list.
     * @param Object path of the Alarm to be searched
     * @return The found Alarm or NULL if the Alarm wasn't found
     */
    public Alarm findAlarm(String objectPath) {

        TimeSampleServer.sendMessage(context, TimeSampleServer.ALARM_EVENT_ACTION, getObjectPath(), "AlarmFactory findAlarm request");
        for (Alarm alarm : alarms) {

            if (alarm.getObjectPath().equals(objectPath)) {

                return alarm;
            }
        }

        return null;
    }
}