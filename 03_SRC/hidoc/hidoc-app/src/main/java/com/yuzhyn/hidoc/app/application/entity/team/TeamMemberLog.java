package com.yuzhyn.hidoc.app.application.entity.team;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMemberLogMapper;
import com.yuzhyn.hidoc.app.system.ibatis.handler.JsonbTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "team_member_log", autoResultMap = true)
public class TeamMemberLog {
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
    private String action;

    public static TeamMemberLog create(String id, String createUserId, TeamMember teamMember, String action) {
        TeamMemberLog teamMemberLog = new TeamMemberLog();
        teamMemberLog.setId(id);
        teamMemberLog.setTeamId(teamMember.getTeamId());
        teamMemberLog.setUserId(teamMember.getUserId());
        if (teamMember.getPermissionRule() != null) teamMemberLog.setPermissionRule(teamMember.getPermissionRule());
        teamMemberLog.setCreateTime(LocalDateTime.now());
        teamMemberLog.setCreateUserId(createUserId);
        teamMemberLog.setAction(action);
        return teamMemberLog;
    }
}
