package com.yuzhyn.hidoc.app.application.entity.serverman;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuzhyn.hidoc.app.application.entity.team.TeamLite;
import com.yuzhyn.hidoc.app.system.ibatis.handler.JsonbTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "server_man_machine", autoResultMap = true)
public class ServerManMachine {
    private String id;
    private String name;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUserId;
    private String updateUserId;
    private String ownerUserId;
    private String type;
    private String address;
    private int port;
    private String username;
    private String password;
    private Boolean isDelete;
    private Boolean portOpen;
    private LocalDateTime portOpenTime;

    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONArray teamIdList;

    // 以下是运行时扩展字段，不存储

    @TableField(exist = false)
    private List<TeamLite> teamList;

    @TableField(exist = false)
    private List<ServerManCmd> cmdList;
}
