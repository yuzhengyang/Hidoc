package com.yuzhyn.hidoc.app.application.entity.team;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuzhyn.hidoc.app.system.ibatis.handler.JsonbTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "team", autoResultMap = true)
public class Team {
    /**
     * 主键ID
     */
    @TableId("id")
    private String id;

    private String name;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime deleteTime;
    private String createUserId;
    private String updateUserId;
    private String deleteUserId;
    private String ownerUserId;
    private Integer memberCount;
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONObject autoJoinRule;
    private Boolean isDelete;

}
