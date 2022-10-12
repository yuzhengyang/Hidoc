package com.yuzhyn.hidoc.app.application.entity.limit;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yuzhyn.hidoc.app.system.ibatis.handler.JsonbTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LimitDose {
    /**
     * 主键id
     */
    private String id;
    /**
     * 接口令牌
     * 用来对接口信息认证，可修改更新
     */
    private String token;
    /**
     * 限制名称
     */
    private String name;
    /**
     * 限制信息描述
     */
    private String description;
    /**
     * 是否启用
     */
    private Boolean isEnable;
    /**
     * 开始生效时间
     */
    private LocalDateTime startTime;
    /**
     * 结束生效时间
     */
    private LocalDateTime stopTime;
    /**
     * 允许小时
     * 多个小时用逗号，分割
     */
    private String allowHours;
    /**
     * 禁止小时
     * 多个小时用逗号，分割
     */
    private String forbidHours;
    /**
     * 允许地址
     * 省，市，区
     */
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONObject allowAddresses;
    /**
     * 禁止地址
     * 省，市，区
     */
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONObject forbidAddresses;
    /**
     * 允许位置（圆形范围）
     * 经度，纬度，范围
     * {longitude 经度-纵向的，latitude 纬度，范围 米}
     */
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONObject allowPositions;
    /**
     * 禁止位置（圆形范围）
     * 经度，纬度，范围
     * {longitude 经度-纵向的，latitude 纬度，范围 米}
     */
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONObject forbidPositions;
    /**
     * 允许账号
     */
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONObject allowAccounts;
    /**
     * 禁止账号
     */
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONObject forbidAccounts;
    /**
     * 允许邮箱后缀
     */
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONObject allowEmailSuffix;
    /**
     * 禁止邮箱后缀
     */
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONObject forbidEmailSuffix;
    /**
     * 允许MAC
     */
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONObject allowMacs;
    /**
     * 禁止MAC
     */
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONObject forbidMacs;
    /**
     * 控制时间单位
     * 年月日时分
     */
    private String ctrlTimeUnit;
    /**
     * 允许时剂量
     * 可以为允许执行次数、时长、数量
     */
    private Long allowDose;
    /**
     * 禁止时剂量
     */
    private Long forbidDose;
    /**
     * 创建人ID
     */
    private String createUserId;
    /**
     * 所属人ID
     */
    private String ownerUserId;
    /**
     * 是否删除
     */
    private Boolean isDelete;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
