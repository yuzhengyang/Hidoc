package com.yuzhyn.hidoc.app.application.schedule;

import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.entity.team.Team;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMemberMapper;
import com.yuzhyn.hidoc.app.application.service.team.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class TeamJoinSchedule {

    @Autowired
    TeamMapper teamMapper;

    @Autowired
    SysUserLiteMapper sysUserLiteMapper;

    @Autowired
    TeamMemberMapper teamMemberMapper;

    @Autowired
    TeamService teamService;

    @Async
    @Scheduled(cron = "0 */10 * * * ?")
    public void job() {
        List<Team> teamList = teamMapper.selectList(null);
        if (!ListTool.ok(teamList)) return;

        for (Team teamItem : teamList) {

            List<SysUserLite> userList = teamMemberMapper.selectUnJoinUsers(teamItem.getId());
            if (!ListTool.ok(userList)) return;

            for (SysUserLite userItem : userList) {

                if (teamItem.getJoinRule() == null) break;
                if (!teamItem.getJoinRule().containsKey("autoJoinEmailSuffix")) break;
                if (!StringTool.ok(teamItem.getJoinRule().getString("autoJoinEmailSuffix"))) break;

                String teamId = teamItem.getId();
                String userId = userItem.getId();
                String type = "autoJoinEmailSuffix";
                String value = "";
                String email = userItem.getEmail();
                String action = "加入团队-JOB";

                teamService.joinTeam(teamId, userId, type, value, email, action, "1");
            }
        }
    }
}
