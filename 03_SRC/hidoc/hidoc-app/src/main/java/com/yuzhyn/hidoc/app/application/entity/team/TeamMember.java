package com.yuzhyn.hidoc.app.application.entity.team;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.system.ibatis.handler.JsonbTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "team_member", autoResultMap = true)
public class TeamMember {
    /**
     * 主键ID
     */
    @TableId("id")
    private String id;
    private String teamId;
    private String userId;

    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONObject permissionRule;

    private LocalDateTime createTime;
    private String createUserId;
    private LocalDateTime updateTime;
    private String updateUserId;

    @TableField(exist = false)
    private SysUserLite userInfo;

}
