package com.yuzhyn.hidoc.app.application.controller4openapi;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocProject;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManCmd;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManMachine;
import com.yuzhyn.hidoc.app.application.entity.team.TeamLite;
import com.yuzhyn.hidoc.app.application.entity.team.TeamMember;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManCmdMapper;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManMachineMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMemberMapper;
import com.yuzhyn.hidoc.app.application.service.team.TeamService;
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
@RequestMapping("openapi/serverMan")
public class ServerManApiController {

    @Autowired
    ServerManMachineMapper serverManMachineMapper;

    @Autowired
    ServerManCmdMapper serverManCmdMapper;

    @Autowired
    TeamService teamService;

    @Autowired
    TeamMemberMapper teamMemberMapper;

    @PostMapping("shareList")
    public ResponseData shareList(@RequestBody Map<String, Object> params) {

        // 如果用户未登录，则直接空，不再展示团队的服务器连接信息
        if (!CurrentUserManager.isLogin()) return ResponseData.ok();

        // 以下为通过登录用户，正式的获取相关的服务器连接信息和指令列表
        // 获取当前用户的团队关系
        List<String> teamIds = new ArrayList<>();
        if (CurrentUserManager.isLogin()) {
            List<TeamMember> memberList = teamMemberMapper.selectList(new LambdaQueryWrapper<TeamMember>().eq(TeamMember::getUserId, CurrentUserManager.getUserId()));
            if (ListTool.ok(memberList))
                teamIds.addAll(memberList.stream().map(TeamMember::getTeamId).distinct().collect(toList()));
        }

        List<ServerManMachine> machineList = new ArrayList<>();
        List<ServerManMachine> machineDbList = serverManMachineMapper.selectList(new LambdaQueryWrapper<ServerManMachine>()
                .eq(ServerManMachine::getIsDelete, false)
                .and(p -> {
                    p.or().eq(ServerManMachine::getOwnerUserId, CurrentUserManager.getUserId());
                    for (String teamId : teamIds) {
                        p.or().apply("jsonb_exists(team_id_list, {0})", teamId);
                    }
                })
                .orderByAsc(ServerManMachine::getName));

        List<ServerManCmd> cmdDbList = serverManCmdMapper.selectList(new LambdaQueryWrapper<ServerManCmd>()
                .eq(ServerManCmd::getIsDelete, false)
                .orderByAsc(ServerManCmd::getName));

        List<TeamLite> allTeams = teamService.getAllTeams();

        for (ServerManMachine machineDb : machineDbList) {

            List<ServerManCmd> cmds = cmdDbList.stream().filter(x -> x.getMachineId().equals(machineDb.getId())).toList();
            if (ListTool.ok(cmds)) {
                machineDb.setCmdList(cmds);
            }

            if (ObjectUtil.isNotEmpty(machineDb.getTeamIdList())) {
                machineDb.setTeamList(teamService.filterTeamsByIds(allTeams, machineDb.getTeamIdList().toJavaList(String.class)));
            }

            machineDb.setUsername("*");
            machineDb.setPassword("*");
            machineList.add(machineDb);
        }

        return ResponseData.okData(machineList);
    }

//    @PostMapping("list")
//    public ResponseData list(@RequestBody Map<String, Object> params) {
//        List<ServerManMachine> serverManMachines = serverManMachineMapper.selectList(new LambdaQueryWrapper<ServerManMachine>()
//                .eq(ServerManMachine::getOwnerUserId, CurrentUserManager.getUser().getId())
//                .eq(ServerManMachine::getIsDelete, false).orderByDesc(ServerManMachine::getCreateTime));
//        ResponseData responseData = ResponseData.ok();
//        responseData.putDataMap("serverManMachines", serverManMachines);
//        return responseData;
//    }
//
//    @PostMapping("get")
//    public ResponseData get(@RequestBody Map<String, Object> params) {
//        ResponseData responseData = ResponseData.ok();
//        if (MapTool.ok(params, "id")) {
//            String id = MapTool.get(params, "id", "").toString();
//            ServerManMachine serverManMachine = serverManMachineMapper.selectById(id);
//            responseData.putDataMap("serverManMachine", serverManMachine);
//        }
//        return responseData;
//    }
}
