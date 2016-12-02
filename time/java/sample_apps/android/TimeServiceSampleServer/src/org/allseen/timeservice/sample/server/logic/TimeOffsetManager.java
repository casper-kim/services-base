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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.allseen.timeservice.Date;
import org.allseen.timeservice.DateTime;
import org.allseen.timeservice.Time;

/**
 * Singleton to manage time offsets. Manages notifications of {@link TimeChangeListener} Utilities to convert from {@link DateTime} to {@link Calendar} and vice versa
 *
 */
public class TimeOffsetManager {

    /**
     * stores the time difference between the system time and the server time.
     */
    private long timeDifference = 0;

    /**
     *  List of  TimeChangeListener registered.
     */
    private final List<TimeChangeListener> timeChangeListenerList = new ArrayList<TimeChangeListener>();


    private static final  TimeOffsetManager instance = new TimeOffsetManager();

    private TimeOffsetManager() {

    }

    public static TimeOffsetManager getInstance() {
        return instance;
    }

    public synchronized void setTimeDifference(long diff) {
        timeDifference = diff;
        for (TimeChangeListener timeChangeListener : timeChangeListenerList) {
            timeChangeListener.timeHasChanged(timeDifference);
        }

    }

    public synchronized long getTimeDifference() {
        return timeDifference;

    }

    public synchronized void registerTimeChangeListener(TimeChangeListener listener) {
        if (!timeChangeListenerList.contains(listener)) {
            timeChangeListenerList.add(listener);
        }
    }

    public synchronized void unRegisterTimeChangeListener(TimeChangeListener listener) {
        if (timeChangeListenerList.contains(listener)) {
            timeChangeListenerList.remove(listener);
        }
    }

    public SimpleDateFormat getDateAndTimeDisplayFormat() {
        return new SimpleDateFormat("HH:mm:ss dd/MM/yyyy",Locale.US);
    }

    public SimpleDateFormat getTimeDisplayFormat() {
        return new SimpleDateFormat("HH:mm:ss",Locale.US);
    }

    public Calendar getCurrentServerTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(c.getTimeInMillis() - timeDifference);
        return c;
    }

    public Calendar convertDateTimeToCalendar(DateTime dt) {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(Calendar.HOUR_OF_DAY, dt.getTime().getHour());
        newCalendar.set(Calendar.MINUTE, dt.getTime().getMinute());
        newCalendar.set(Calendar.SECOND, dt.getTime().getSeconds());
        newCalendar.set(Calendar.MILLISECOND, dt.getTime().getMilliseconds());
        newCalendar.set(Calendar.MONTH, dt.getDate().getMonth() - 1);
        newCalendar.set(Calendar.YEAR, dt.getDate().getYear());
        newCalendar.set(Calendar.DAY_OF_MONTH, dt.getDate().getDay());
        return newCalendar;
    }

    public DateTime convertCalendarToDateTime(Calendar currentCalendar) {
        Time ts = new org.allseen.timeservice.Time((byte) currentCalendar.get(Calendar.HOUR_OF_DAY),
                                                   (byte) currentCalendar.get(Calendar.MINUTE),
                                                   (byte) currentCalendar.get(Calendar.SECOND),
                                                   (short) currentCalendar.get(Calendar.MILLISECOND));

        Date ds = new Date((short) currentCalendar.get(Calendar.YEAR),
                           (byte) (currentCalendar.get(Calendar.MONTH) + 1),
                           (byte) currentCalendar.get(Calendar.DAY_OF_MONTH));

        return new DateTime(ds, ts, (short) 0);
    }

}