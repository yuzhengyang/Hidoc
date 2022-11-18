package com.yuzhyn.hidoc.app.application.controller.team;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.doc.DocComment;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManMachine;
import com.yuzhyn.hidoc.app.application.entity.team.Team;
import com.yuzhyn.hidoc.app.application.entity.team.TeamMember;
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

    @PostMapping("create")
    public ResponseData create(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "name")) {
            String name = MapTool.getString(params, "name", "");
            String description = MapTool.getString(params, "description", "");

            if (StringTool.ok(name)) {
                Team team = new Team();
                team.setId(R.SnowFlake.nexts());
                team.setName(name);
                team.setDescription(description);
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
        if (MapTool.ok(params, "id", "name" )) {
            String id = MapTool.get(params, "id", "").toString();
            String name = MapTool.get(params, "name", "").toString();
            String description = MapTool.get(params, "description", "").toString();

            if (StringTool.ok(id, name)) {
                Team team = teamMapper.selectById(id);
                if (team != null) {
                    // 非所属者不能编辑
                    if (!team.getOwnerUserId().equals(CurrentUserManager.getUser().getId())) {
                        return ResponseData.error("编辑失败，非所属者不能编辑");
                    }
                    team.setName(name);
                    team.setDescription(description);
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

    /**
     * 我加入的团队
     *
     * @param params
     * @return
     */
    @PostMapping("myJoinTeam")
    public ResponseData myJoinTeam(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        List<TeamMember> teamMembers = teamMemberMapper.selectList(new LambdaQueryWrapper<TeamMember>().eq(TeamMember::getUserId, CurrentUserManager.getUserId()));
        if (ListTool.ok(teamMembers)) {
            List<String> ids = teamMembers.stream().map(TeamMember::getTeamId).distinct().collect(toList());
            List<Team> teams = teamMapper.selectList(new LambdaQueryWrapper<Team>().in(Team::getId, ids));

            if (ListTool.ok(teams)) {
                responseData.putData(teams);
            }
        }
        return responseData;
    }

    /**
     * 属于我的团队
     *
     * @param params
     * @return
     */
    @PostMapping("myOwnerTeam")
    public ResponseData myOwnerTeam(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        List<Team> teams = teamMapper.selectList(new LambdaQueryWrapper<Team>().eq(Team::getOwnerUserId, CurrentUserManager.getUserId()));
        if (ListTool.ok(teams)) {
            responseData.putData(teams);
        }
        return responseData;
    }
}
