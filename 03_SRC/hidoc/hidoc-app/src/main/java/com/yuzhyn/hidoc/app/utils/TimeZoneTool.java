package com.yuzhyn.hidoc.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeZoneTool {

    private static String timeZone;

    public static String getTimeZone() {
        if (timeZone == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("Z");
            sdf.setTimeZone(TimeZone.getDefault());
            timeZone = sdf.format(new Date());
        }
        return timeZone;
    }
}
