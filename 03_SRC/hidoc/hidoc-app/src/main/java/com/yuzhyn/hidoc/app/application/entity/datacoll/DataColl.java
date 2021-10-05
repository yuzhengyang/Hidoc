package com.yuzhyn.hidoc.app.application.entity.datacoll;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.yuzhyn.hidoc.app.system.ibatis.handler.JsonbTypeHandler;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.PGobject;

import java.time.LocalDateTime;

@Data
@TableName(value = "data_coll", autoResultMap = true)
public class DataColl {
    /**
     * 主键ID
     */
    @TableId("id")
    private String id;

    /**
     * 数据创建时间
     * 在源设备的生成时间
     */
    private LocalDateTime createTime;

    /**
     * 所属数据收集计划ID
     */
    private String planId;

    /**
     * 数据来源
     * 示例：平台开发辅助工具、数据库执行工具
     */
    private String dataSource;

    /**
     * 客户端类型
     * 示例：Windows、Android、Web
     */
    private String clientType;

    /**
     * 请求来的IP地址
     * 服务自动获取
     */
    private String ip;

    /**
     * 设备MAC地址
     * 发送方自行填写
     * 示例：00-D8-61-D6-07-2C
     */
    private String mac;

    /**
     * 发送者身份唯一标识
     * 示例：37021320200202XXXX
     */
    private String senderId;

    /**
     * 发送者姓名
     * 示例：张无忌
     */
    private String senderName;

    /**
     * 数据类型
     * 示例：CPU监控数据、内存监控数据、应用程序使用空间监控数据
     */
    private String dataType;

    /**
     * 数据
     * 示例：
     * {
     *     "times": "32",
     *     "funName": "代码生成器"
     * }
     */
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONObject data;
}
