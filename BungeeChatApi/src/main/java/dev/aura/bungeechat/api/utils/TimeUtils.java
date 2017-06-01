package dev.aura.bungeechat.api.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.experimental.UtilityClass;

/**
 * A util class that exists to simply get date and time values like the year or
 * the current timestamp.
 */
@UtilityClass
public class TimeUtils {
    /**
     * Gets the current timestamp in 24h format without seconds and double
     * digits for both hour and minute, separated by a colon.
     *
     * @return The timestamp formatted like: <code>"HH:mm"</code>
     * @see SimpleDateFormat
     */
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");
        Date now = new Date();

        return sdfDate.format(now);
    }

    /**
     * Retuns the day in the month formatted to always have two digits.
     *
     * @return The date formatted like: <code>"dd"</code>
     * @see SimpleDateFormat
     */
    public static String getCurrentDay() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd");
        Date now = new Date();

        return sdfDate.format(now);
    }

    /**
     * Retuns the month in the year formatted to always have two digits.
     *
     * @return The date formatted like: <code>"MM"</code>
     * @see SimpleDateFormat
     */
    public static String getCurrentMonth() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM");
        Date now = new Date();

        return sdfDate.format(now);
    }

    /**
     * Retuns the the year formatted to always have four digits.
     *
     * @return The date formatted like: <code>"yyyy"</code>
     * @see SimpleDateFormat
     */
    public static String getCurrentYear() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy");
        Date now = new Date();

        return sdfDate.format(now);
    }
}
