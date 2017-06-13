package dev.aura.bungeechat.api.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import lombok.experimental.UtilityClass;

/**
 * A util class that exists to simply get date and time values like the year or
 * the current timestamp.
 */
@UtilityClass
public class TimeUtil {
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

    /**
     * Gets the time value as a double from a String.
     *
     * @param stringedTime
     *            The time as string to convert
     * @return double The time as double
     */
    public static double convertStringTimeToDouble(String stringedTime) {
        double timeamount = 0;

        if (stringedTime.contains("y")) {
            timeamount = Double.valueOf(stringedTime.replaceAll("y", "")) * TimeUnit.DAYS.toMillis(365);
        } else if (stringedTime.contains("mo")) {
            timeamount = Double.valueOf(stringedTime.replaceAll("mo", "")) * TimeUnit.DAYS.toMillis(30);
        } else if (stringedTime.contains("w")) {
            timeamount = Double.valueOf(stringedTime.replaceAll("w", "")) * TimeUnit.DAYS.toMillis(7);
        } else if (stringedTime.contains("d")) {
            timeamount = Double.valueOf(stringedTime.replaceAll("d", "")) * TimeUnit.DAYS.toMillis(1);
        } else if (stringedTime.contains("h")) {
            timeamount = Double.valueOf(stringedTime.replaceAll("h", "")) * TimeUnit.HOURS.toMillis(1);
        } else if (stringedTime.contains("m")) {
            timeamount = Double.valueOf(stringedTime.replaceAll("m", "")) * TimeUnit.MINUTES.toMillis(1);
        } else if (stringedTime.contains("s")) {
            timeamount = Double.valueOf(stringedTime.replaceAll("s", "")) * TimeUnit.SECONDS.toMillis(1);
        }

        return timeamount;
    }

}
