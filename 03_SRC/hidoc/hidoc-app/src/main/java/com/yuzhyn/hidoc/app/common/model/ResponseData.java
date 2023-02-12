package com.yuzhyn.hidoc.app.common.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseData {
    private String token;
    private String msg;
    private int code;
    private long count;
    private long total;
    private Map<String, Object> meta = new HashMap();
    private List data = new ArrayList();
    private Integer popUp;
    private String status;
    private String toast;

    @JSONField(serialize = false)
    private boolean useDateFormat = false;

    @JSONField(serialize = false)
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";

    public ResponseData() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToast() {
        return toast;
    }

    public void setToast(String toast) {
        this.toast = toast;
    }

    public Integer getPopUp() {
        return this.popUp;
    }

    public void setPopUp(Integer popUp) {
        this.popUp = popUp;
    }

    public String getMsg() {
        return this.msg;
    }

    public int getCode() {
        return this.code;
    }

    public long getCount() {
        return this.count;
    }

    public Map<String, Object> getMeta() {
        return this.meta;
    }

    public List<Object> getData() {
        return this.data;
    }

    public ResponseData putDataMap(String key, Object value) {
        this.meta.put(key, value);
        return this;
    }

    public ResponseData putData(List value) {
        this.data.addAll(value);
        return this;
    }

    public ResponseData putData(List value, long total) {
        this.data.addAll(value);
        this.total = total;
        return this;
    }

    public ResponseData changeMap(Map<String, Object> map) {
        this.meta = map;
        return this;
    }

    public ResponseData(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ResponseData(int code, String msg, long count) {
        this.code = code;
        this.msg = msg;
        this.count = count;
    }

    public static ResponseData ok() {
        return new ResponseData(0, "Ok");
    }

    public static ResponseData error() {
        return new ResponseData(1, "ERROR");
    }

    public static ResponseData exist() {
        return new ResponseData(2, "tip");
    }

    public static ResponseData ok(String msg) {
        return new ResponseData(0, msg);
    }

    public static ResponseData error(String msg) {
        return new ResponseData(1, msg);
    }

    public static ResponseData okData(String key, Object value) {
        ResponseData data = new ResponseData(0, "Ok");
        data.putDataMap(key, value);
        return data;
    }

    public static ResponseData okData(List value, long count) {
        ResponseData data = new ResponseData(0, "Ok", count);
        if (value != null) {
            data.putData(value);
        }

        return data;
    }

    public static ResponseData okData(List value) {
        ResponseData data = new ResponseData(0, "Ok");
        if (value != null) {
            data.putData(value);
        }

        return data;
    }

    public static ResponseData okMap(Map<String, Object> map) {
        ResponseData data = new ResponseData(0, "Ok");
        return data.changeMap(map);
    }

    public static ResponseData notFound() {
        return new ResponseData(404, "暂无相关数据");
    }

    public static ResponseData badRequest() {
        return new ResponseData(400, "参数有误");
    }

    public static ResponseData forbidden() {
        return new ResponseData(403, "Forbidden");
    }

    public static ResponseData unauthorized() {
        return new ResponseData(401, "unauthorized");
    }

    public static ResponseData serviceException(int code, String msg) {
        return new ResponseData(code, msg);
    }

    @JSONField(serialize = false)
    public boolean isUseDateFormat() {
        return this.useDateFormat;
    }

    public void setUseDateFormat(boolean useDateFormat) {
        this.useDateFormat = useDateFormat;
    }

    @JSONField(serialize = false)
    public String getDateFormat() {
        return this.dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String toJSONString() {
        return this.isUseDateFormat() ? JSON.toJSONString(this, new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat}) : JSON.toJSONString(this);
    }
}