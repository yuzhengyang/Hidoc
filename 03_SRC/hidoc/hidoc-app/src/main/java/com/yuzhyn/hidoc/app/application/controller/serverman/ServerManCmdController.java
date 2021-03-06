package com.yuzhyn.hidoc.app.application.controller.serverman;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManCmd;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManExeLog;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManMachine;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManCmdMapper;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManExeLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManMachineMapper;
import com.yuzhyn.hidoc.app.application.model.serverman.CmdRunLog;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("serverManCmd")
public class ServerManCmdController {

    @Autowired
    ServerManMachineMapper serverManMachineMapper;

    @Autowired
    ServerManCmdMapper serverManCmdMapper;

    @Autowired
    ServerManExeLogMapper serverManExeLogMapper;

    @PostMapping("create")
    public ResponseData create(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "machineId", "name", "description", "type")) {
            String machineId = MapTool.get(params, "machineId", "").toString();
            String name = MapTool.get(params, "name", "").toString();
            String description = MapTool.get(params, "description", "").toString();
            String type = MapTool.get(params, "type", "").toString();
            String contentTa = MapTool.get(params, "contentTa", "").toString();
            String contentTb = MapTool.get(params, "contentTb", "").toString();
            String contentTc = MapTool.get(params, "contentTc", "").toString();

            if (StringTool.ok(name, machineId)) {
                ServerManCmd serverManCmd = new ServerManCmd();
                serverManCmd.setId(R.SnowFlake.nexts());
                serverManCmd.setName(name);
                serverManCmd.setDescription(description);
                serverManCmd.setCreateTime(LocalDateTime.now());
                serverManCmd.setUpdateTime(null);
                serverManCmd.setCreateUserId(CurrentUserManager.getUser().getId());
                serverManCmd.setUpdateUserId(null);
                serverManCmd.setOwnerUserId(CurrentUserManager.getUser().getId());
                serverManCmd.setType(type);
                serverManCmd.setContentTa(contentTa);
                serverManCmd.setContentTb(contentTb);
                serverManCmd.setContentTc(contentTc);
                serverManCmd.setIsDelete(false);
                serverManCmd.setMachineId(machineId);

                int flag = serverManCmdMapper.insert(serverManCmd);
                if (flag > 0) {
//                    info.setPassword("*");
                    return ResponseData.okData("serverManCmd", serverManCmd);
                }
            }
        }
        return ResponseData.error("??????????????????????????????");
    }

    @PostMapping("edit")
    public ResponseData edit(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id", "machineId", "name", "description", "type")) {
            String id = MapTool.get(params, "id", "").toString();
            String machineId = MapTool.get(params, "machineId", "").toString();
            String name = MapTool.get(params, "name", "").toString();
            String description = MapTool.get(params, "description", "").toString();
            String type = MapTool.get(params, "type", "").toString();
            String contentTa = MapTool.get(params, "contentTa", "").toString();
            String contentTb = MapTool.get(params, "contentTb", "").toString();
            String contentTc = MapTool.get(params, "contentTc", "").toString();

            if (StringTool.ok(id, name)) {
                ServerManCmd serverManCmd = serverManCmdMapper.selectById(id);
                if (serverManCmd != null) {
                    // ????????????????????????
                    if (!serverManCmd.getOwnerUserId().equals(CurrentUserManager.getUser().getId())) {
                        return ResponseData.error("???????????????????????????????????????");
                    }
                    serverManCmd.setName(name);
                    serverManCmd.setDescription(description);
                    serverManCmd.setUpdateTime(LocalDateTime.now());
                    serverManCmd.setUpdateUserId(CurrentUserManager.getUser().getId());
                    serverManCmd.setType(type);
                    serverManCmd.setContentTa(contentTa);
                    serverManCmd.setContentTb(contentTb);
                    serverManCmd.setContentTc(contentTc);
                    serverManCmd.setMachineId(machineId);

                    int flag = serverManCmdMapper.updateById(serverManCmd);
                    if (flag > 0) {
//                        info.setPassword("*");
                        return ResponseData.okData("serverManCmd", serverManCmd);
                    }
                }
            }
        }
        return ResponseData.error("??????????????????????????????");
    }


    @PostMapping("delete")
    public ResponseData delete(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id", "token")) {
            String id = MapTool.get(params, "id", "").toString();
            String token = MapTool.get(params, "token", "").toString();

            if (StringTool.ok(id, token)) {
                ServerManCmd serverManCmd = serverManCmdMapper.selectById(id);
                if (serverManCmd != null) {
                    // ????????????????????????
                    if (!serverManCmd.getOwnerUserId().equals(CurrentUserManager.getUser().getId())) {
                        return ResponseData.error("???????????????????????????????????????");
                    }

                    serverManCmd.setIsDelete(true);
                    int flag = serverManCmdMapper.updateById(serverManCmd);
                    if (flag > 0) {
//                        info.setPassword("*");
                        return ResponseData.okData("serverManCmd", serverManCmd);
                    }
                }
            }
        }
        return ResponseData.error("??????????????????????????????");
    }

    @PostMapping("list")
    public ResponseData list(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        if (MapTool.ok(params, "machineId")) {
            String machineId = MapTool.get(params, "machineId", "").toString();
            List<ServerManCmd> serverManCmds = serverManCmdMapper.selectList(new LambdaQueryWrapper<ServerManCmd>()
                    .eq(ServerManCmd::getOwnerUserId, CurrentUserManager.getUser().getId())
                    .eq(ServerManCmd::getMachineId, machineId).eq(ServerManCmd::getIsDelete, false)
                    .orderByAsc(ServerManCmd::getName));
            responseData.putDataMap("serverManCmds", serverManCmds);
        }
        return responseData;
    }

    @PostMapping("get")
    public ResponseData get(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        if (MapTool.ok(params, "id")) {
            String id = MapTool.get(params, "id", "").toString();
            ServerManCmd serverManCmd = serverManCmdMapper.selectById(id);
            responseData.putDataMap("serverManCmd", serverManCmd);
        }
        return responseData;
    }


    @PostMapping("run")
    public ResponseData run(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id", "token")) {
            String id = MapTool.get(params, "id", "").toString();
            String token = MapTool.get(params, "token", "").toString();
            LocalDateTime beginTime = LocalDateTime.now();
            String dialogId = R.SnowFlake.nexts();

            if (StringTool.ok(id, token)) {
                ServerManCmd cmd = serverManCmdMapper.selectById(id);
                if (cmd != null) {
                    ServerManMachine machine = serverManMachineMapper.selectById(cmd.getMachineId());

                    if (machine != null) {

                        // ??????ssh??????
                        if (machine.getType().equals("ssh")) {
                            boolean createStat = R.SshManager.create(dialogId, machine.getAddress(), machine.getPort(), machine.getUsername(), machine.getPassword(), null);
                            boolean openChannelStat = R.SshManager.openChannel(dialogId);

                            if (createStat == false || openChannelStat == false) {
                                return ResponseData.error("???????????????createStat:" + createStat + ",openChannelStat:" + openChannelStat);
                            }

                            R.SshManager.readChannel(dialogId, (bytes) -> {
//                                String s = new String(bytes);
                                System.out.println("server man cmd run: " + new String(bytes));
                                R.Queues.CmdRunLogQueue.add(new CmdRunLog(dialogId, bytes));
                            });

                            try {
                                R.SshManager.sendCommandRun(dialogId, cmd.getContentTa());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        // ??????????????????
                        ServerManExeLog serverManExeLog = new ServerManExeLog();
                        serverManExeLog.setId(dialogId);
                        serverManExeLog.setCmdId(cmd.getId());
                        serverManExeLog.setMachineId(machine.getId());
                        serverManExeLog.setRunUserId(CurrentUserManager.getUser().getId());
                        serverManExeLog.setBeginTime(beginTime);
                        serverManExeLog.setEndTime(LocalDateTime.now());
                        serverManExeLog.setDialogId(dialogId);
                        serverManExeLog.setContentTa(cmd.getContentTa());
                        serverManExeLog.setContentTb(cmd.getContentTb());
                        serverManExeLog.setContentTc(cmd.getContentTc());
                        serverManExeLog.setResultTa("");
                        serverManExeLog.setResultTb("");
                        serverManExeLog.setResultTc("");
                        serverManExeLogMapper.insert(serverManExeLog);
                        return ResponseData.ok("????????????????????????????????????????????????");
                    }

                }
            }
        }
        return ResponseData.error("???????????????????????????????????????");
    }

}
