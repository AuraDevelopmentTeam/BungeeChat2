package dev.aura.bungeechat.api.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeUtils {

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");
        Date now = new Date();
        return sdfDate.format(now);
    }

    public static String getCurrentDay() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd");
        Date now = new Date();
        return sdfDate.format(now);
    }

    public static String getCurrentMonth() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("MMM");
        Date now = new Date();
        return sdfDate.format(now);
    }

    public static String getCurrentYear() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy");
        Date now = new Date();
        return sdfDate.format(now);
    }

}
