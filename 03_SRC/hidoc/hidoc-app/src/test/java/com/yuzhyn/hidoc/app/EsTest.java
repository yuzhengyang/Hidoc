package com.yuzhyn.hidoc.app;

import co.elastic.clients.elasticsearch._types.mapping.GeoPointProperty;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.hidoc.app.utils.EsTool;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class EsTest {
    public static void main(String[] args) throws IOException {
        try{
            String index = "test.103";
            EsTool esTool = new EsTool("https://192.168.14.184:9004", "elastic", "x5676bca-aOEqBZz28K5", "hidoc.");
            esTool.createClient();

            Map<String, Property> properties = new HashMap<>();
            properties.put("location", Property.of(p -> p.geoPoint(GeoPointProperty.of(g -> g))));
            esTool.createIndex(index, properties);

            String id = String.valueOf(System.currentTimeMillis());
            esTool.createDocument(index, id, Map.of(
                    "name", "test",
                    "location", "30.0,120.0",
                    "createTime", DateTimeFormat.toStr(LocalDateTime.now(), DateTimeFormatPattern.NORMAL_DATETIME_TIMEZONE)));

            esTool.createIndex(index, properties);

            id = String.valueOf(System.currentTimeMillis());
            esTool.createDocument(index, id, Map.of(
                    "name", "test",
                    "location", "40.0,120.0",
                    "createTime", DateTimeFormat.toStr(LocalDateTime.now(), DateTimeFormatPattern.NORMAL_DATETIME_TIMEZONE)));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
