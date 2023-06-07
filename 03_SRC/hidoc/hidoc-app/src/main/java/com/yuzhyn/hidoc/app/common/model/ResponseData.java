package com.yuzhyn.hidoc.app.common.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yuzhyn.hidoc.app.common.enums.ResponseCode;
import org.apache.commons.lang3.ObjectUtils;

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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
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
        if (ObjectUtils.isNotEmpty(value)) this.count = value.size();
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

    public ResponseData(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
    }

    public ResponseData(ResponseCode responseCode, String appendMsg) {
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg() + " [" + appendMsg + "]";
    }

    private ResponseData(int code, String msg, long count) {
        this.code = code;
        this.msg = msg;
        this.count = count;
    }

    public static ResponseData ok() {
        return new ResponseData(ResponseCode.SUCCESS);
    }

    public static ResponseData error() {
        return new ResponseData(ResponseCode.ERROR);
    }

//    public static ResponseData exist() {
//        return new ResponseData(2, "tip");
//    }

    public static ResponseData ok(String msg) {
        return new ResponseData(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static ResponseData error(String msg) {
        return new ResponseData(ResponseCode.ERROR.getCode(), msg);
    }

    public static ResponseData okData(String key, Object value) {
        ResponseData data = new ResponseData(ResponseCode.SUCCESS);
        data.putDataMap(key, value);
        return data;
    }

    public static ResponseData okData(List value, long count) {
        ResponseData data = new ResponseData(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg(), count);
        if (value != null) {
            data.putData(value);
        }

        return data;
    }

    public static ResponseData okData(List value) {
        ResponseData data = new ResponseData(ResponseCode.SUCCESS);
        if (value != null) {
            data.putData(value);
        }

        return data;
    }

    public static ResponseData okMap(Map<String, Object> map) {
        ResponseData data = new ResponseData(ResponseCode.SUCCESS);
        return data.changeMap(map);
    }

//    public static ResponseData notFound() {
//        return new ResponseData(ResponseCode.NO_RESOURCE);
//    }

//    public static ResponseData badRequest() {
//        return new ResponseData(ResponseCode.PARAMS_ERROR);
//    }

//    public static ResponseData forbidden() {
//        return new ResponseData(ResponseCode.ACCESS_FORBIDDEN);
//    }

//    public static ResponseData unauthorized() {
//        return new ResponseData(ResponseCode.UNAUTHORIZED);
//    }

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