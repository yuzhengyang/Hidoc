package com.yuzhyn.hidoc.app.application.controller.team;

import com.alibaba.fastjson.JSONObject;
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

    @Transactional
    @PostMapping("create")
    public ResponseData create(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "teamId", "type", "value")) {
            String teamId = MapTool.getString(params, "teamId", "");
            String type = MapTool.getString(params, "type", "");
            String value = MapTool.getString(params, "value", "");
            String email = CurrentUserManager.getUser().getEmail();
            String action = "加入团队";

            Team team = teamMapper.selectById(teamId);
            if (team == null) return ResponseData.error("团队信息不存在");
            if (team.getJoinRule() == null) return ResponseData.error("加入规则未配置");

            switch (type) {
                case "userJoinPassword":
                    String password = team.getJoinRule().getString("userJoinPassword");
                    if (!(StringTool.ok(password, value) && password.equals(value))) return ResponseData.error("加入密码错误");
                    action = "加入团队（通过密码）";
                    break;
                case "autoJoinEmailSuffix":
                    String emailSuffix = team.getJoinRule().getString("autoJoinEmailSuffix");
                    if (!(StringTool.ok(emailSuffix, email) && email.endsWith("@" + emailSuffix))) return ResponseData.error("不符合加入邮箱规则");
                    action = "加入团队（通过邮箱规则）";
                    break;
            }

            TeamMember teamMember = new TeamMember();
            teamMember.setId(R.SnowFlake.nexts());
            teamMember.setTeamId(teamId);
            teamMember.setUserId(CurrentUserManager.getUserId());
            teamMember.setCreateTime(LocalDateTime.now());
            teamMember.setCreateUserId(CurrentUserManager.getUserId());
            int flag = teamMemberMapper.insert(teamMember);
            if (flag > 0) {
                TeamMemberLog teamMemberLog = TeamMemberLog.create(R.SnowFlake.nexts(), CurrentUserManager.getUserId(), teamMember, action);
                teamMemberLogMapper.insert(teamMemberLog);

                teamMapper.updateMemberCount(teamId);
                return ResponseData.ok();
            }
        }
        return ResponseData.error("加入失败，请检查信息");
    }

    @Transactional
    @PostMapping("invite")
    public ResponseData invite(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "teamId", "email")) {
            String teamId = MapTool.getString(params, "teamId", "");
            String email = MapTool.getString(params, "email", "");

            Team team = teamMapper.selectById(teamId);
            if (team == null) return ResponseData.error("团队信息不存在");

            SysUserLite userLite = sysUserLiteMapper.selectOne(new LambdaQueryWrapper<SysUserLite>().eq(SysUserLite::getEmail, email));
            if (userLite == null) return ResponseData.error("用户信息不存在");

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
                return ResponseData.ok();
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
