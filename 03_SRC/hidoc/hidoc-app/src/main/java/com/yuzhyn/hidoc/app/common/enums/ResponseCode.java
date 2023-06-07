package com.yuzhyn.hidoc.app.common.enums;

/**
 * 关于code的规划
 * 10-29 登录权限相关
 * 30-49 参数验证相关
 * 100-999 三位数空出，避免和网络错误码产生沟通语义上的冲突
 * 1000-9999 是系统扩展标志
 * 90000 及以上为个业务随意使用code标志
 */
public enum ResponseCode {

    SUCCESS(0, "操作成功"),
    ERROR(1, "操作失败"),

    LOGIN_TIMEOUT(10, "您的账号已登出或状态过期，请重新登录"),
    ACCESS_NEED_LOGIN(11, "您访问的内容需要登录才能查看，请先登录系统"),
    UNAUTHORIZED(12, "您正在访问未经授权的内容，具体请联系内容发布方"),

//    NO_RESOURCE(4, "暂无相关数据"),
//    PARAMS_ERROR(5, "参数有误"),
//    ACCESS_FORBIDDEN(403, "拒绝访问受限内容"),
//    UNAUTHORIZED(401, "unauthorized"),
//    PARAMS_MISS(9001, "请求缺少参数"),
//    ACCESS_LIMIT_CONTENT(50008, "拒绝访问受限内容，请登录访问！"),
    ;
    private int code;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code, String msg) {
        this.code = code;

    }

    public int getCode() {
        return code;
    }

    private ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}