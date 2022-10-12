package com.yuzhyn.hidoc.app.application.entity.limit;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yuzhyn.hidoc.app.system.ibatis.handler.JsonbTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LimitAccess {
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
     * 允许小时时段
     * 多个小时用逗号，分割
     */
    private String allowHours;
    /**
     * 禁止小时时段
     * 多个小时用逗号，分割
     */
    private String forbidHours;
    /**
     * 允许地址
     * 省-市-区，省-市-区，省-市
     */
    private String allowAddresses;
    /**
     * 禁止地址
     * 省-市-区，省-市-区，省
     */
    private String forbidAddresses;
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
    private String allowAccounts;
    /**
     * 禁止账号
     */
    private String forbidAccounts;
    /**
     * 允许邮箱后缀
     */
    private String allowEmailSuffix;
    /**
     * 禁止邮箱后缀
     */
    private String forbidEmailSuffix;
    /**
     * 允许MAC
     */
    private String allowMacs;
    /**
     * 禁止MAC
     */
    private String forbidMacs;
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
