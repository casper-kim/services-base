 /******************************************************************************
  *   * 
  *    Copyright (c) 2016 Open Connectivity Foundation and AllJoyn Open
  *    Source Project Contributors and others.
  *    
  *    All rights reserved. This program and the accompanying materials are
  *    made available under the terms of the Apache License, Version 2.0
  *    which accompanies this distribution, and is available at
  *    http://www.apache.org/licenses/LICENSE-2.0

  ******************************************************************************/

package org.allseen.timeservice;

import java.util.EnumSet;
import java.util.Set;

import org.allseen.timeservice.ajinterfaces.Alarm;

/**
 * The Schedule indicates the when the {@link Alarm} should fire.
 */
public class Schedule {

    /**
     * Days of week used to define Alarm schedule
     */
    public static enum WeekDay {

        SUNDAY ((byte) 0x1),
        MONDAY ((byte) 0x2),
        TUESDAY ((byte) 0x4),
        WEDNESDAY((byte) 0x8),
        THURSDAY ((byte) 0x10),
        FRIDAY ((byte) 0x20),
        SATURDAY ((byte) 0x40),
        ;

        /**
         * Week Day type type
         */
        private final byte TYPE_VALUE;

        /**
         * Constructor
         * @param type Type value
         */
        private WeekDay(byte type) {

            TYPE_VALUE = type;
        }

        /**
         * Returns the numeric value of the {@link WeekDay}
         * @return week day numeric value
         */
        public byte getValue() {

            return TYPE_VALUE;
        }

        /**
         * Create a {@link Set} of {@link WeekDay}s from the given BitMap
         * @param bitMap Bit Map of the day of week
         * @return {@link Set} of the days of week
         */
        public static Set<WeekDay> getWeekDays(byte bitMap) {

            Set<WeekDay> weekDays = EnumSet.noneOf(WeekDay.class);

            for ( WeekDay weekDay : WeekDay.values() ) {

                if ( (weekDay.TYPE_VALUE & bitMap) == weekDay.TYPE_VALUE )  {

                    weekDays.add(weekDay);
                }
            }

            return weekDays;
        }

        /**
         * Return bit map from the given {@link Set} of the {@link WeekDay}
         * @param weekDays {@link Set} of days of week
         * @return Selected weekdays bitmap representation
         */
        public static byte getBitMap(Set<WeekDay> weekDays) {

            byte bitMask = 0;

            for (WeekDay weekDay : weekDays) {

                bitMask |= weekDay.TYPE_VALUE;
            }

            return bitMask;
        }
    }

    //=======================================//

    /**
     * Schedule time
     */
    private final Time time;

    /**
     * Days of week when an alarm should fire
     */
    private final Set<WeekDay> weekDays;

    /**
     * Use this constructor to define an Alarm {@link Schedule}.
     * @param time Schedule {@link Time}
     * @param weekDays Days of week when an Alarm should fire.
     * Empty weekDays means that the Alarm is fired only once.
     * @throws IllegalArgumentException Is thrown if undefined arguments have been received
     */
    public Schedule(Time time, Set<WeekDay> weekDays) {

        if ( time == null ) {

            throw new IllegalArgumentException("Undefined time");
        }

        if ( weekDays == null ) {

            throw new IllegalArgumentException("Undefined weekDays");
        }

        this.time     = time;
        this.weekDays = weekDays;
    }

    /**
     * Use this constructor to define a {@link Schedule} for an Alarm that should fire only once.
     * @param time Schedule {@link Time}.
     * @throws IllegalArgumentException Is thrown if undefined arguments have been received
     */
    public Schedule(Time time) {

        this(time, EnumSet.noneOf(WeekDay.class));
    }

    /**
     * Get schedule {@link Time}
     * @return {@link Time}
     */
    public Time getTime() {

        return time;
    }

    /**
     * Get schedule days of week
     * @return {@link Set} of {@link WeekDay}s.
     */
    public Set<WeekDay> getWeekDays() {

        return weekDays;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return String.format("{Schedule Time: '%s', WeekDays: '%s'}", time, weekDays);
    }
}