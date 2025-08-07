package com.yuzhyn.hidoc.app.application.controller.team;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.entity.team.Team;
import com.yuzhyn.hidoc.app.application.entity.team.TeamMember;
import com.yuzhyn.hidoc.app.application.entity.team.TeamMemberLog;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMemberLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMemberMapper;
import com.yuzhyn.hidoc.app.application.service.team.TeamService;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("teamMember")
public class TeamMemberController {

    @Autowired
    TeamMapper teamMapper;

    @Autowired
    TeamMemberMapper teamMemberMapper;

    @Autowired
    TeamMemberLogMapper teamMemberLogMapper;

    @Autowired
    SysUserLiteMapper sysUserLiteMapper;

    @Autowired
    TeamService teamService;

    @PostMapping("list")
    public ResponseData list(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "teamId")) {
            String teamId = MapTool.getString(params, "teamId", "");
            List<TeamMember> members = teamMemberMapper.selectList(new LambdaQueryWrapper<TeamMember>().eq(TeamMember::getTeamId, teamId));
            if (ListTool.ok(members)) {
                List<String> userIds = members.stream().map(TeamMember::getUserId).collect(toList());
                List<SysUserLite> users = sysUserLiteMapper.selectList(new LambdaQueryWrapper<SysUserLite>().in(SysUserLite::getId, userIds));
                if (ListTool.ok(users)) {
                    members.forEach(member -> {
                        users.forEach(user -> {
                            if (member.getUserId().equals(user.getId())) {
                                member.setUserInfo(user);
                            }
                        });
                    });
                }
                return ResponseData.okData(members);
            }
        }
        return ResponseData.ok();
    }

    @PostMapping("create")
    public ResponseData create(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "teamId", "type", "value")) {
            String teamId = MapTool.getString(params, "teamId", "");
            String type = MapTool.getString(params, "type", "");
            String value = MapTool.getString(params, "value", "");
            String email = CurrentUserManager.getUser().getEmail();
            String action = "加入团队";

            return teamService.joinTeam(teamId, CurrentUserManager.getUserId(), type, value, email, action, CurrentUserManager.getUserId());

        }
        return ResponseData.error("加入失败，请检查信息");
    }

    @Transactional
    @PostMapping("invite")
    public ResponseData invite(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "teamId", "email")) {
            String teamId = MapTool.getString(params, "teamId", "");
            String emailInput = MapTool.getString(params, "email", "");

            emailInput = emailInput.replaceAll("\n",",");
            emailInput = emailInput.replaceAll("，",",");
            String[] emailArray = StringTool.split(emailInput, ",", true, true, true);

            int flagCount = 0;
            for (String emailItem : emailArray) {
                String email = emailItem.trim();
                Team team = teamMapper.selectById(teamId);
                if (team == null) return ResponseData.error("团队信息不存在");

                SysUserLite userLite = sysUserLiteMapper.selectOne(new LambdaQueryWrapper<SysUserLite>().eq(SysUserLite::getEmail, email).eq(SysUserLite::getIsFrozen, false));
                if (userLite == null) {
//                    return ResponseData.error("用户信息不存在");
                    continue;
                }

                if (!teamMemberMapper.exists(new LambdaQueryWrapper<TeamMember>().eq(TeamMember::getTeamId, teamId).eq(TeamMember::getUserId, userLite.getId()))) {
                    TeamMember teamMember = new TeamMember();
                    teamMember.setId(R.SnowFlake.nexts());
                    teamMember.setTeamId(teamId);
                    teamMember.setUserId(userLite.getId());
                    teamMember.setCreateTime(LocalDateTime.now());
                    teamMember.setCreateUserId(CurrentUserManager.getUserId());
                    int flag = teamMemberMapper.insert(teamMember);
                    if (flag > 0) {
                        TeamMemberLog teamMemberLog = TeamMemberLog.create(R.SnowFlake.nexts(), CurrentUserManager.getUserId(), teamMember, "邀请加入");
                        teamMemberLogMapper.insert(teamMemberLog);
                        teamMapper.updateMemberCount(teamId);
                        flagCount += flag;
                    }
                }
            }

            if (flagCount > 0) {
                return ResponseData.ok("邀请成功，本次邀请人数：" + flagCount);
            }
        }
        return ResponseData.error("邀请失败，请检查信息");
    }


    @PostMapping("edit")
    public ResponseData edit(@RequestBody Map<String, Object> params) {
        return ResponseData.error("编辑失败，请完善信息");
    }

    @Transactional
    @PostMapping("delete")
    public ResponseData delete(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "teamId")) {
            String teamId = MapTool.get(params, "teamId", "").toString();
            String userId = CurrentUserManager.getUserId();
            if (StringTool.ok(teamId, userId)) {

                LambdaQueryWrapper wrapper = new LambdaQueryWrapper<TeamMember>().eq(TeamMember::getTeamId, teamId).eq(TeamMember::getUserId, userId);

                List<TeamMember> members = teamMemberMapper.selectList(wrapper);
                if (ListTool.ok(members)) {
                    TeamMemberLog teamMemberLog = TeamMemberLog.create(R.SnowFlake.nexts(), CurrentUserManager.getUserId(), members.get(0), "退出团队");
                    teamMemberLogMapper.insert(teamMemberLog);

                    teamMemberMapper.delete(wrapper);
                    teamMapper.updateMemberCount(teamId);
                    return ResponseData.ok();
                }
            }
        }
        return ResponseData.error("退出失败，请重新选择");
    }
}
