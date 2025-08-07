package com.yuzhyn.hidoc.app.application.service.team;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.team.Team;
import com.yuzhyn.hidoc.app.application.entity.team.TeamLite;
import com.yuzhyn.hidoc.app.application.entity.team.TeamMember;
import com.yuzhyn.hidoc.app.application.entity.team.TeamMemberLog;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMemberLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMemberMapper;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    @Autowired
    TeamMapper teamMapper;

    @Autowired
    TeamMemberMapper teamMemberMapper;

    @Autowired
    TeamMemberLogMapper teamMemberLogMapper;

    @Autowired
    SysUserLiteMapper sysUserLiteMapper;

    @Autowired
    TeamLiteMapper teamLiteMapper;

    @Transactional
    public ResponseData joinTeam(String teamId, String userId, String type, String value, String email, String action, String createUserId) {
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

    public boolean isMember(String teamIds, String userId) {
        if(ObjectUtil.isEmpty(teamIds)) return false;
        List<TeamMember> teamMemberList = teamMemberMapper.selectList(new LambdaQueryWrapper<TeamMember>().eq(TeamMember::getUserId, userId));
        if (ListTool.ok(teamMemberList)) {
            for (TeamMember teamMember : teamMemberList) {
                if (teamIds.contains(teamMember.getTeamId())) return true;
            }
        }
        return false;
    }

    public List<TeamLite> getAllTeams() {
        return teamLiteMapper.selectList(null);
    }

    public List<TeamLite> filterTeamsByIds(List<TeamLite> teams, String ids) {
        List<TeamLite> result = new ArrayList<>();
        String[] teamsCodeArray = StringTool.split(ids, ",", true, true, true);
        if (ListTool.ok(teamsCodeArray) && ListTool.ok(teams)) {
            for (String teamCode : teamsCodeArray) {
                for (TeamLite team : teams) {
                    if (teamCode.equals(team.getId())) result.add(new TeamLite(team.getId(), team.getName()));
                }
            }
        }
        return result;
    }
}
