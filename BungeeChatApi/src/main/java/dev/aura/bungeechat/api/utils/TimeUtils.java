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
     * Gets the current timestamp in 24h format with date and double digits for
     * both hour and minute, separated by a colon.
     *
     * @return The timestamp formatted like: <code>"yyyy/MM/dd HH:mm:ss"</code>
     * @see SimpleDateFormat
     */
    public static String getLongTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date now = new Date();

        return sdfDate.format(now);
    }

    /**
     * Gets the current timestamp in 24h format and double digits for both hour
     * and minute, separated by a colon.
     *
     * @return The timestamp formatted like: <code>"HH:mm:ss"</code>
     * @see SimpleDateFormat
     */
    public static String getTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();

        return sdfDate.format(now);
    }

    /**
     * Gets the current timestamp in 24h format without seconds and double
     * digits for both hour and minute, separated by a colon.
     *
     * @return The timestamp formatted like: <code>"HH:mm"</code>
     * @see SimpleDateFormat
     */
    public static String getShortTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");
        Date now = new Date();

        return sdfDate.format(now);
    }

    /**
     * Gets the date in the month formatted to always have two digits. And the
     * zear with four.
     *
     * @return The date formatted like: <code>"yyyy/MM/dd"</code>
     * @see SimpleDateFormat
     */
    public static String getDate() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");
        Date now = new Date();

        return sdfDate.format(now);
    }

    /**
     * Gets the day in the month formatted to always have two digits.
     *
     * @return The date formatted like: <code>"dd"</code>
     * @see SimpleDateFormat
     */
    public static String getDay() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd");
        Date now = new Date();

        return sdfDate.format(now);
    }

    /**
     * Gets the month in the year formatted to always have two digits.
     *
     * @return The date formatted like: <code>"MM"</code>
     * @see SimpleDateFormat
     */
    public static String getMonth() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM");
        Date now = new Date();

        return sdfDate.format(now);
    }

    /**
     * Gets the the year formatted to always have four digits.
     *
     * @return The date formatted like: <code>"yyyy"</code>
     * @see SimpleDateFormat
     */
    public static String getYear() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy");
        Date now = new Date();

        return sdfDate.format(now);
    }
}
