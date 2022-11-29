package com.yuzhyn.hidoc.app.application.controller.team;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.doc.DocComment;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManMachine;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.entity.team.Team;
import com.yuzhyn.hidoc.app.application.entity.team.TeamLite;
import com.yuzhyn.hidoc.app.application.entity.team.TeamMember;
import com.yuzhyn.hidoc.app.application.entity.team.TeamMemberLog;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMemberMapper;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("team")
public class TeamController {

    @Autowired
    TeamMapper teamMapper;

    @Autowired
    TeamMemberMapper teamMemberMapper;

    @Autowired
    SysUserLiteMapper sysUserLiteMapper;

    @Autowired
    TeamLiteMapper teamLiteMapper;

    @PostMapping("create")
    public ResponseData create(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "name")) {
            String name = MapTool.getString(params, "name", "");
            String description = MapTool.getString(params, "description", "");
            Object joinRuleObject = MapTool.get(params, "joinRule", null);

            if (StringTool.ok(name)) {
                Team team = new Team();
                team.setId(R.SnowFlake.nexts());
                team.setName(name);
                team.setDescription(description);
                if (joinRuleObject != null && joinRuleObject instanceof JSONObject) team.setJoinRule((JSONObject) joinRuleObject);
                team.setCreateTime(LocalDateTime.now());
                team.setCreateUserId(CurrentUserManager.getUserId());
                team.setOwnerUserId(CurrentUserManager.getUserId());
                team.setMemberCount(0);
                team.setIsDelete(false);

                int flag = teamMapper.insert(team);
                if (flag > 0) {
                    return ResponseData.okData("team", team);
                }
            }
        }
        return ResponseData.error("创建失败，请完善信息");
    }

    @PostMapping("edit")
    public ResponseData edit(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id", "name")) {
            String id = MapTool.get(params, "id", "").toString();
            String name = MapTool.get(params, "name", "").toString();
            String description = MapTool.get(params, "description", "").toString();
            Object joinRuleObject = MapTool.get(params, "joinRule", null);

            if (StringTool.ok(id, name)) {
                Team team = teamMapper.selectById(id);
                if (team != null) {
                    // 非所属者不能编辑
                    if (!team.getOwnerUserId().equals(CurrentUserManager.getUser().getId())) {
                        return ResponseData.error("编辑失败，非所属者不能编辑");
                    }
                    team.setName(name);
                    team.setDescription(description);
                    if (joinRuleObject != null && joinRuleObject instanceof JSONObject) team.setJoinRule((JSONObject) joinRuleObject);
                    team.setUpdateTime(LocalDateTime.now());
                    team.setUpdateUserId(CurrentUserManager.getUser().getId());

                    int flag = teamMapper.updateById(team);
                    if (flag > 0) {
                        return ResponseData.okData("team", team);
                    }
                }
            }
        }
        return ResponseData.error("编辑失败，请完善信息");
    }

    @PostMapping("delete")
    public ResponseData delete(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id", "token")) {
            String id = MapTool.get(params, "id", "").toString();
            String token = MapTool.get(params, "token", "").toString();

            if (StringTool.ok(id, token)) {
                Team team = teamMapper.selectById(id);
                if (team != null) {

                    // 存在成员，不能删除
                    if (team.getMemberCount() > 0) {
                        return ResponseData.error("存在成员，不能删除");
                    }
                    // 非所属者不能删除
                    if (!team.getOwnerUserId().equals(CurrentUserManager.getUser().getId())) {
                        return ResponseData.error("删除失败，非所属者不能删除");
                    }

                    team.setDeleteTime(LocalDateTime.now());
                    team.setDeleteUserId(CurrentUserManager.getUserId());
                    team.setIsDelete(true);
                    int flag = teamMapper.updateById(team);
                    if (flag > 0) {
                        return ResponseData.okData("team", team);
                    }
                }
            }
        }
        return ResponseData.error("删除失败，请重新选择");
    }


    @PostMapping("get")
    public ResponseData get(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id")) {
            String id = MapTool.get(params, "id", "").toString();
            Team team = teamMapper.selectById(id);
            return ResponseData.okData("team", team);
        }
        return ResponseData.error("未查询到数据");
    }

    @PostMapping("getMyJoinTeams")
    public ResponseData getMyJoinTeams(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();

        List<String> teamIds = new ArrayList<>();
        List<TeamMember> memberList = teamMemberMapper.selectList(new LambdaQueryWrapper<TeamMember>().eq(TeamMember::getUserId, CurrentUserManager.getUserId()));
        if (ListTool.ok(memberList)) teamIds.addAll(memberList.stream().map(TeamMember::getTeamId).distinct().collect(toList()));

        if (ListTool.ok(teamIds)) {
            List<TeamLite> teams = teamLiteMapper.selectList(new LambdaQueryWrapper<TeamLite>().in(TeamLite::getId, teamIds).orderByAsc(TeamLite::getName));
            if (ListTool.ok(teams)) {
                responseData.putData(teams);
            }
        }
        return responseData;
    }

    /**
     * 属于我的团队（我管理的团队）
     *
     * @param params
     * @return
     */
    @PostMapping("getOwnerTeams")
    public ResponseData getOwnerTeams(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        List<Team> teams = teamMapper.selectList(new LambdaQueryWrapper<Team>().eq(Team::getOwnerUserId, CurrentUserManager.getUserId()).orderByAsc(Team::getName));
        if (ListTool.ok(teams)) {
            for (Team item : teams) {
                SysUserLite ownerUser = sysUserLiteMapper.selectById(item.getOwnerUserId());
                if (ownerUser != null) item.setOwnerUser(ownerUser);
            }
            responseData.putData(teams);
        }
        return responseData;
    }

    /**
     * 其他团队
     *
     * @param params
     * @return
     */
    @PostMapping("getAllTeams")
    public ResponseData getAllTeams(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        List<Team> teamList = teamMapper.selectList(new LambdaQueryWrapper<Team>().orderByAsc(Team::getName));
        if (ListTool.ok(teamList)) {
            List<TeamMember> memberList = teamMemberMapper.selectList(new LambdaQueryWrapper<TeamMember>().eq(TeamMember::getUserId, CurrentUserManager.getUserId()));

            for (Team teamItem : teamList) {

                // 补充是否加入状态
                teamItem.setMyJoinStatus("n");
                if (ListTool.ok(memberList)) {
                    for (TeamMember memberItem : memberList) {
                        if (teamItem.getId().equals(memberItem.getTeamId())) {
                            teamItem.setMyJoinStatus("y");
                            break;
                        }
                    }
                }

                // 补充所属用户信息
                SysUserLite ownerUser = sysUserLiteMapper.selectById(teamItem.getOwnerUserId());
                if (ownerUser != null) teamItem.setOwnerUser(ownerUser);
            }

            responseData.putData(teamList);
        }

        return responseData;
    }
}
