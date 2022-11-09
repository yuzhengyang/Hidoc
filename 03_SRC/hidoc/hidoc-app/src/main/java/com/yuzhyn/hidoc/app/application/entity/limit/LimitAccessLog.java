package com.yuzhyn.hidoc.app.application.entity.limit;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuzhyn.hidoc.app.system.ibatis.handler.JsonbTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "limit_access_log")
public class LimitAccessLog {
    /**
     * 主键id
     */
    private String id;
    /**
     * 通断限制ID
     */
    private String limitAccessId;
    private String address;
    private String version;
    private String account;
    private String machine;
    private String hour;
    private String position;
    private String email;
    private String mac;
    private Boolean isAccess;
    private String message;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
