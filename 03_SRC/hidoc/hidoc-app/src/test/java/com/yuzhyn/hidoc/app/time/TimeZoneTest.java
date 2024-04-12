package com.yuzhyn.hidoc.app.time;

import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class TimeZoneTest {
    public static void main(String[] args) {
        // 创建一个Date对象，表示当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("Z");
        sdf.setTimeZone(TimeZone.getDefault());
        String formattedDate = sdf.format(new Date());
        System.out.println(formattedDate);


        TimeZone timeZone = TimeZone.getDefault();
        ZoneId zoneId = ZoneId.systemDefault();
        System.out.println(zoneId.getRules().toString());

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String format = sdf1.format(new Date());
        System.out.println(format);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss+0800");
        String s = df.format(LocalDateTime.now());
        System.out.println(s);
    }
}
