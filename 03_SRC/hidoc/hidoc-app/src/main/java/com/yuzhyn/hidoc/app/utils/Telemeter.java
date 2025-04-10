package com.yuzhyn.hidoc.app.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.azylee.core.systems.bases.SystemPropertyTool;
import com.yuzhyn.hidoc.app.aarg.R;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.time.LocalDateTime;

public class Telemeter {
    public static void send() {
        String URL = "https://www.yuzhyn.top/hidoc_server_api/datacoll/create";
        try {
            JSONObject data = new JSONObject();
            data.put("a","a");
            data.put("javaVmName", System.getProperty("java.vm.name"));
            data.put("javaVmVersion", System.getProperty("java.vm.version"));
            data.put("javaVmVendor" , System.getProperty("java.vm.vendor"));

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", "f769a638c28d4f3c81f99eb248bba019e03862ec47cb4888885096fdc968033a");
            jsonObject.put("dataSource", R.AppName);
            jsonObject.put("clientType", System.getProperty("os.name"));
            jsonObject.put("mac", "略");
            jsonObject.put("senderId", R.MachineId);
            jsonObject.put("senderName", "略");
            jsonObject.put("senderMachine", "略");
            jsonObject.put("senderAccount", System.getProperty("user.name"));
            jsonObject.put("data", data.toJSONString());
            jsonObject.put("createTime", DateTimeFormat.toStr(LocalDateTime.now(), DateTimeFormatPattern.NORMAL_DATETIME));
            jsonObject.put("dataType", "telemeter");

            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.get("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(jsonObject.toJSONString(), JSON);
            Request request = new Request.Builder()
                    .url(URL)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (Exception ex) {
        }
    }

    public static void main(String[] args) {
        send();
    }
}
