package com.yuzhyn.hidoc.app.application.controller4openapi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocProject;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManCmd;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManMachine;
import com.yuzhyn.hidoc.app.application.entity.team.TeamLite;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManCmdMapper;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManMachineMapper;
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

    @PostMapping("shareList")
    public ResponseData shareList(@RequestBody Map<String, Object> params) {

        String machineId = MapTool.getString(params, "machineId", "");

        List<ServerManMachine> machineList = new ArrayList<>();
        List<ServerManMachine> machineDbList = serverManMachineMapper.selectList(new LambdaQueryWrapper<ServerManMachine>()
                .eq(ServerManMachine::getIsDelete, false)
                .orderByAsc(ServerManMachine::getName));

        List<ServerManCmd> cmdDbList = serverManCmdMapper.selectList(new LambdaQueryWrapper<ServerManCmd>()
                .eq(ServerManCmd::getIsDelete, false)
                .orderByAsc(ServerManCmd::getName));

        List<TeamLite> allTeams = teamService.getAllTeams();

        for (ServerManMachine machineDb : machineDbList) {
            boolean isAdd = false;
            // 展示存在团队执行信息的内容
            isAdd = StringTool.ok(machineDb.getTeamsExecute());
            // 展示属于登录用户的内容
            if (!isAdd) {
                isAdd = CurrentUserManager.isLogin() && CurrentUserManager.getUserId().equals(machineDb.getOwnerUserId());
            }

            List<ServerManCmd> cmds = cmdDbList.stream().filter(x -> x.getMachineId().equals(machineDb.getId())).toList();
            if (isAdd && ListTool.ok(cmds)) {
                if (StringTool.ok(machineDb.getTeamsExecute())) {
                    machineDb.setTeamExecuteList(teamService.filterTeamsByIds(allTeams, machineDb.getTeamsExecute()));
                }

                machineDb.setUsername("*");
                machineDb.setPassword("*");
                machineDb.setCmdList(cmds);
                machineList.add(machineDb);
            }
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
