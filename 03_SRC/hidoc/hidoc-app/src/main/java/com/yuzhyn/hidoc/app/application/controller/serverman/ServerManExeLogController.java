package com.yuzhyn.hidoc.app.application.controller.serverman;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringConst;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.azylee.core.ios.txts.TxtTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManCmd;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManExeLog;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManOutput;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManCmdMapper;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManExeLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManOutputMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("serverManExeLog")
public class ServerManExeLogController {

    @Autowired
    SysUserLiteMapper sysUserLiteMapper;

    @Autowired
    ServerManCmdMapper serverManCmdMapper;

    @Autowired
    ServerManExeLogMapper serverManExeLogMapper;

    @Autowired
    ServerManOutputMapper serverManOutputMapper;

    @PostMapping({"pageList", "pl"})
    public ResponseData pageList(@RequestBody Map<String, Object> params) {
        int current = MapTool.getInt(params, "current", 1);
        int size = MapTool.getInt(params, "size", 1);

        List<ServerManCmd> serverManCmds = serverManCmdMapper.selectList(new LambdaQueryWrapper<ServerManCmd>()
                .eq(ServerManCmd::getIsDelete, false)
                .eq(ServerManCmd::getOwnerUserId, CurrentUserManager.getUser().getId()));

        if (ListTool.ok(serverManCmds)) {
            List<String> cmdIds = serverManCmds.stream().map(ServerManCmd::getId).distinct().collect(toList());

            IPage<ServerManExeLog> serverManExeLogIPage = serverManExeLogMapper.selectPage(new Page<ServerManExeLog>(current, size),
                    new LambdaQueryWrapper<ServerManExeLog>()
                            .in(ServerManExeLog::getCmdId, cmdIds)
                            .orderByDesc(ServerManExeLog::getBeginTime));
            List<ServerManExeLog> list = serverManExeLogIPage.getRecords();
            if (ListTool.ok(list)) {
                // 查询运行SSH脚本的用户信息
                List<String> runUserIdList = list.stream().map(ServerManExeLog::getRunUserId).collect(toList());
                List<SysUserLite> sysUserLiteList = sysUserLiteMapper.selectByIds(runUserIdList);
                if (ListTool.ok(sysUserLiteList)) {
                    for (ServerManExeLog serverManExeLog : list) {
                        for (SysUserLite sysUserLite : sysUserLiteList) {
                            if (sysUserLite.getId().equals(serverManExeLog.getRunUserId())) {
                                serverManExeLog.setRunUser(sysUserLite);
                                break;
                            }
                        }
                    }
                }
                return ResponseData.okData(list);
            } else {
                return ResponseData.ok();
            }
        } else {
            return ResponseData.ok();
        }
    }

    @PostMapping({"fileDetail"})
    public ResponseData fileDetail(@RequestBody Map<String, Object> params) {
        String id = MapTool.getString(params, "id", "");
        ServerManExeLog serverManExeLog = serverManExeLogMapper.selectById(id);
        String file = DirTool.combine(R.Paths.ServerManSsh, id + ".txt");
        List<String> lines = TxtTool.readLine(file);
        StringBuilder fileDetail = new StringBuilder();
        if (ListTool.ok(lines)) {
            for (String l : lines) {
                fileDetail.append(l);
                fileDetail.append(StringConst.NEWLINE);
            }
        }
        ResponseData responseData = ResponseData.ok();
        responseData.putDataMap("fileDetail", fileDetail.toString());
        return responseData;
    }

    @PostMapping({"serverManOutput"})
    public ResponseData serverManOutput(@RequestBody Map<String, Object> params) {
        String dialogId = MapTool.getString(params, "dialogId", "");
        Long serialNumber = MapTool.getLong(params, "serialNumber", 0L);
        if (ObjectUtil.isEmpty(dialogId) || serialNumber == 0) {
            return ResponseData.ok();
        }
        List<ServerManOutput> list = serverManOutputMapper.selectList(new LambdaQueryWrapper<ServerManOutput>()
                .eq(ServerManOutput::getDialogId, dialogId)
                .ge(ServerManOutput::getSerialNumber, serialNumber));
        ResponseData responseData = ResponseData.ok();
        responseData.putData(list);
        return responseData;
    }
}
