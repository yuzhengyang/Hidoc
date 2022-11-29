package com.yuzhyn.hidoc.app.application.service.team;

import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.team.Team;
import com.yuzhyn.hidoc.app.application.entity.team.TeamMember;
import com.yuzhyn.hidoc.app.application.entity.team.TeamMemberLog;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMemberLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMemberMapper;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TeamMemberService {

    @Autowired
    TeamMapper teamMapper;

    @Autowired
    TeamMemberMapper teamMemberMapper;

    @Autowired
    TeamMemberLogMapper teamMemberLogMapper;

    @Autowired
    SysUserLiteMapper sysUserLiteMapper;

    @Transactional
    public ResponseData create(String teamId,String userId, String type, String value, String email, String action, String createUserId) {
        Team team = teamMapper.selectById(teamId);
        if (team == null) return ResponseData.error("团队信息不存在");
        if (team.getJoinRule() == null) return ResponseData.error("加入规则未配置");

        switch (type) {
            case "userJoinPassword":
                String password = team.getJoinRule().getString("userJoinPassword");
                if (!(StringTool.ok(password, value) && password.equals(value))) return ResponseData.error("加入密码错误");
                action += "（通过密码）";
                break;
            case "autoJoinEmailSuffix":
                String emailSuffix = team.getJoinRule().getString("autoJoinEmailSuffix");
                if (!(StringTool.ok(emailSuffix, email) && email.endsWith("@" + emailSuffix))) return ResponseData.error("不符合加入邮箱规则");
                action += "（通过邮箱规则）";
                break;
        }

        TeamMember teamMember = new TeamMember();
        teamMember.setId(R.SnowFlake.nexts());
        teamMember.setTeamId(teamId);
        teamMember.setUserId(userId);
        teamMember.setCreateTime(LocalDateTime.now());
        teamMember.setCreateUserId(createUserId);
        int flag = teamMemberMapper.insert(teamMember);
        if (flag > 0) {
            TeamMemberLog teamMemberLog = TeamMemberLog.create(R.SnowFlake.nexts(), createUserId, teamMember, action);
            teamMemberLogMapper.insert(teamMemberLog);

            teamMapper.updateMemberCount(teamId);
            return ResponseData.ok();
        }
        return ResponseData.error("加入失败，请检查信息");
    }
}
