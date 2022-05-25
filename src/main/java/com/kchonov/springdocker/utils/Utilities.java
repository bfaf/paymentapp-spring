package com.kchonov.springdocker.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 *
 * @author Krasi
 */
public class Utilities {

    public static Date fixTimezone(long timestamp) {
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(timestamp);

        return calendar.getTime();
    }
    
}
