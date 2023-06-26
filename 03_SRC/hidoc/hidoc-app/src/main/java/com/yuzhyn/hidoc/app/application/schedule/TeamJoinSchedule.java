package com.yuzhyn.hidoc.app.application.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.datas.datetimes.LocalDateTimeTool;
import com.yuzhyn.azylee.core.datas.exceptions.ExceptionTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.azylee.core.ios.txts.PropertyTool;
import com.yuzhyn.azylee.core.systems.bases.SystemStatusTool;
import com.yuzhyn.azylee.core.systems.models.SystemStatusInfo;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.doc.DocAccessLog;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileDownloadLog;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManExeLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysAccessLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysStatusLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.entity.team.Team;
import com.yuzhyn.hidoc.app.application.entity.team.TeamMember;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocAccessLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileDownloadLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileMapper;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManExeLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysAccessLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysStatusLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMemberMapper;
import com.yuzhyn.hidoc.app.application.service.team.TeamMemberService;
import com.yuzhyn.hidoc.app.utils.ssh.SshClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    TeamMemberService teamMemberService;

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

                teamMemberService.create(teamId, userId, type, value, email, action, "1");
            }
        }
    }
}
