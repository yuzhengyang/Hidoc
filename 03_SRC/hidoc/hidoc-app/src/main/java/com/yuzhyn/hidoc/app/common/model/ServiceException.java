package com.yuzhyn.hidoc.app.common.model;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuzhyn.hidoc.app.common.enums.ResponseCode;
import org.slf4j.Logger;
public class ServiceException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -6971716908203238516L;

    /**
     * 日志对象.
     */
    private Logger logger;

    /**
     * 错误编码.
     */
    private int code = 12345678;

    /**
     * 异常消息
     */
    private String msg;

    /**
     * 结果异常码.
     */
    private ResponseCode responseCode;



    /**
     * 错误消息的补充内容.
     */
    private String messageSupplement = "";

    /**
     * 构造函数
     * Creates a new instance of ServiceException.
     * @param message 异常描述
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     *
     * Creates a new instance of ServiceException.
     *
     * @param message 异常描述
     * @param logger 当前类的日志对象
     */
    public ServiceException(String message, Logger logger) {
        super(message);
        this.logger = logger;
    }

    /**
     *
     * Creates a new instance of ServiceException.
     *
     * @param cause Throwable异常
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance of ServiceException.
     *
     * @param code
     *            异常码
     * @param message
     *            异常描述
     */
    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * Creates a new instance of ServiceException.
     *
     * @param code
     *            异常码
     * @param message
     *            异常描述
     * @param logger
     *            当前类的日志对象
     */
    public ServiceException(int code, String message, Logger logger) {
        super(message);
        this.code = code;
        this.logger = logger;
    }

    /**
     *
     * Creates a new instance of ServiceException.
     *
     * @param cause Throwable异常
     * @param logger 当前类的日志对象
     */
    public ServiceException(Throwable cause, Logger logger) {
        super(cause);
        this.logger = logger;
    }

    /**
     *
     * Creates a new instance of ServiceException.
     *
     * @param message 异常描述
     * @param cause Throwable异常
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * Creates a new instance of ServiceException.
     *
     * @param message 异常描述
     * @param cause Throwable异常
     * @param logger 当前类的日志对象
     */
    public ServiceException(String message, Throwable cause, Logger logger) {
        super(message, cause);
        this.logger = logger;
    }

    /**
     *
     * Creates a new instance of ServiceException.
     *
     * @param responseCode 异常码
     */
    public ServiceException(ResponseCode responseCode) {
        super(responseCode.getMsg());
        this.code = responseCode.getCode();
        this.responseCode = responseCode;
    }

    /**
     *
     * Creates a new instance of ServiceException.
     *
     * @param code 异常码
     * @param message 异常描述
     * @param cause Throwable异常
     */
    public ServiceException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     *
     * Creates a new instance of ServiceException.
     *
     * @param code 异常码
     * @param message 异常描述
     * @param cause Throwable异常
     * @param logger 当前类的日志对象
     */
    public ServiceException(int code, String message, Throwable cause, Logger logger) {
        super(message, cause);
        this.code = code;
        this.logger = logger;
    }

    /**
     *
     * Creates a new instance of ServiceException.
     *
     * @param code 异常码
     * @param message 异常描述
     * @param messageSupplement 异常描述
     */
    public ServiceException(int code, String message, String messageSupplement) {
        super(messageSupplement + message);
        this.code = code;
        this.messageSupplement = messageSupplement;
    }

    /**
     *
     * Creates a new instance of ServiceException.
     *
     * @param code 异常码
     * @param message 异常描述
     * @param messageSupplement 异常描述
     * @param logger 当前类的日志对象
     */
    public ServiceException(int code, String message, String messageSupplement, Logger logger) {
        super(messageSupplement + message);
        this.code = code;
        this.messageSupplement = messageSupplement;
        this.logger = logger;
    }

    /**
     *
     * Creates a new instance of ServiceException.
     *
     * @param code 异常码
     * @param message 异常描述
     * @param cause Throwable异常
     * @param messageSupplement 异常描述
     */
    public ServiceException(int code, String message, Throwable cause, String messageSupplement) {
        super(messageSupplement + message, cause);
        this.code = code;
        this.messageSupplement = messageSupplement;
    }

    /**
     *
     * Creates a new instance of ServiceException.
     *
     * @param code 异常码
     * @param message 异常描述
     * @param cause Throwable异常
     * @param messageSupplement 异常描述
     * @param logger 当前类的日志对象
     */
    public ServiceException(int code, String message, Throwable cause, String messageSupplement, Logger logger) {
        super(messageSupplement + message, cause);
        this.code = code;
        this.messageSupplement = messageSupplement;
        this.logger = logger;
    }

    /**
     *
     * Creates a new instance of ServiceException.
     *
     * @param value 其他值
     * @param code 异常码
     * @param message 异常描述
     * @param messageSupplement 异常描述
     * @param logger 当前类的日志对象
     */
    public ServiceException(Object value, int code, String message, String messageSupplement, Logger logger) {
        super(messageSupplement + ":" + toJSONString(value) + message);
        this.code = code;
        this.messageSupplement = messageSupplement;
        this.logger = logger;
    }

    /**
     *
     * Creates a new instance of ServiceException.
     *
     * @param value 其他值
     * @param code 异常码
     * @param message 异常描述
     * @param cause Throwable异常
     * @param messageSupplement 异常描述
     */
    public ServiceException(Object value, int code, String message, Throwable cause, String messageSupplement) {
        super(messageSupplement + ":" + toJSONString(value) + message, cause);
        this.code = code;
        this.messageSupplement = messageSupplement;
    }

    /**
     *
     * Creates a new instance of ServiceException.
     *
     * @param value 其他值
     * @param code 异常码
     * @param message 异常描述
     * @param cause Throwable异常
     * @param messageSupplement 异常描述
     * @param logger 当前类的日志对象
     */
    public ServiceException(Object value, int code, String message, Throwable cause, String messageSupplement,
                            Logger logger) {
        super(messageSupplement + ":" + toJSONString(value) + message, cause);
        this.code = code;
        this.messageSupplement = messageSupplement;
        this.logger = logger;
    }

    /**
     * 对象转为json字符串
     *
     * @param obj 对象
     * @return json字符串
     */
    private static String toJSONString(Object obj) {
        return null == obj ? "" : JSONObject.toJSONString(obj);
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessageSupplement(String messageSupplement) {
        this.messageSupplement = messageSupplement;
    }

    public int getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMessageSupplement() {
        return messageSupplement;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

}