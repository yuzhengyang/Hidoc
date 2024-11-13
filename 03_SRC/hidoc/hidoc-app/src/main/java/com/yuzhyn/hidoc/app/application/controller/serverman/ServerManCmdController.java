package com.yuzhyn.hidoc.app.application.controller.serverman;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManCmd;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManExeLog;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManMachine;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManCmdMapper;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManExeLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManMachineMapper;
import com.yuzhyn.hidoc.app.application.model.serverman.CmdRunLog;
import com.yuzhyn.hidoc.app.application.service.team.TeamService;
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

    @Autowired
    TeamService teamService;

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
        return ResponseData.error("创建失败，请完善信息");
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
                    // 非所属者不能编辑
                    if (!serverManCmd.getOwnerUserId().equals(CurrentUserManager.getUser().getId())) {
                        return ResponseData.error("编辑失败，非所属者不能编辑");
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
        return ResponseData.error("编辑失败，请完善信息");
    }


    @PostMapping("delete")
    public ResponseData delete(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id", "token")) {
            String id = MapTool.get(params, "id", "").toString();
            String token = MapTool.get(params, "token", "").toString();

            if (StringTool.ok(id, token)) {
                ServerManCmd serverManCmd = serverManCmdMapper.selectById(id);
                if (serverManCmd != null) {
                    // 非所属者不能删除
                    if (!serverManCmd.getOwnerUserId().equals(CurrentUserManager.getUser().getId())) {
                        return ResponseData.error("删除失败，非所属者不能删除");
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
        return ResponseData.error("删除失败，请重新选择");
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

                        // 判断执行间隔，防止频繁操作引发的未知问题，默认间隔5分钟
                        Page<ServerManExeLog> logPage = serverManExeLogMapper.selectPage(new Page<ServerManExeLog>(1, 1),
                                new LambdaQueryWrapper<ServerManExeLog>()
                                        .eq(ServerManExeLog::getCmdId, id)
                                        .orderByDesc(ServerManExeLog::getBeginTime));
                        if (logPage.getTotal() > 0) {
                            Long cmdInterval = cmd.getInterval() == null ? 300 : cmd.getInterval(); // 执行间隔时间
                            LocalDateTime lastTime = logPage.getRecords().get(0).getBeginTime(); // 上次执行时间
                            LocalDateTime allowTime = lastTime.plusSeconds(cmdInterval); // 本次允许的执行时间
                            if (beginTime.isBefore(allowTime)) {
                                return ResponseData.error("执行间隔过短，请在 " + DateTimeFormat.toStr(allowTime, DateTimeFormatPattern.NORMAL_DATETIME) + " 后再执行。");
                            }
                        }

                        // 这里判断权限，团队权限（命令共享），个人权限
                        if(!teamService.isMember(machine.getTeamsExecute(), CurrentUserManager.getUserId())){
                            return ResponseData.error("您不在所属的团队中，没有操作权限，详情请咨询管理员。");
                        }

                        // 执行ssh命令
                        if (machine.getType().equals("ssh")) {
                            boolean createStat = R.SshManager.create(dialogId, machine.getAddress(), machine.getPort(), machine.getUsername(), machine.getPassword(), null);
                            boolean openChannelStat = R.SshManager.openChannel(dialogId);

                            if (createStat == false || openChannelStat == false) {
                                return ResponseData.error("执行失败，createStat:" + createStat + ",openChannelStat:" + openChannelStat);
                            }

                            R.SshManager.readChannel(dialogId, (bytes) -> {
                                // 这里不用读取，直接扔到队列定时处理就行
                                R.Queues.CmdRunLogQueue.add(new CmdRunLog(dialogId, bytes));
                            });

                            try {
                                R.SshManager.sendCommandRun(dialogId, cmd.getContentTa());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        // 记录执行日志
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
                        ResponseData result = ResponseData.ok("执行成功，详细输出信息请查看日志");
                        result.putDataMap("log", serverManExeLog);
                        return result;
                    }

                }
            }
        }
        return ResponseData.error("执行失败，执行指令必须登录");
    }

}
