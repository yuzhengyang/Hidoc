package com.yuzhyn.hidoc.app.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class TaobaoTime {

    private static final String GET_TIMESTAMP = "http://api.m.taobao.com/rest/api3.do?api=mtop.common.getTimestamp";

    /**
     * 获取时间戳
     * @return {"api":"mtop.common.getTimestamp","v":"*","ret":["SUCCESS::接口调用成功"],"data":{"t":"1660185051494"}}
     */
    public static String getTimeStampJson() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(GET_TIMESTAMP).build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        } catch (IOException ex) {
        }
        return "";
    }

    public static long getTimeStamp() {
        try {
            String tsTxt = TaobaoTime.getTimeStampJson();
            if (StringTool.ok(tsTxt)) {
                JSONObject jsonObject = JSON.parseObject(tsTxt);
                JSONObject data = jsonObject.getJSONObject("data");
                String t = data.getString("t");
                long webTs = Long.parseLong(t);
                return webTs;
            }
        } catch (Exception ex) {
        }
        return 0;
    }
}
