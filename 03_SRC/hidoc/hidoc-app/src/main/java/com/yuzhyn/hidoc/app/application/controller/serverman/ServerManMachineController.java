package com.yuzhyn.hidoc.app.application.controller.serverman;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManCmd;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManMachine;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManMachineMapper;
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

@Slf4j
@RestController
@RequestMapping("serverManMachine")
public class ServerManMachineController {

    @Autowired
    ServerManMachineMapper serverManMachineMapper;

    @PostMapping("create")
    public ResponseData create(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "name", "description", "type", "address", "port", "username", "password")) {
            String name = MapTool.get(params, "name", "").toString();
            String description = MapTool.get(params, "description", "").toString();
            String type = MapTool.get(params, "type", "").toString();
            String address = MapTool.get(params, "address", "").toString();
            int port = MapTool.getInt(params, "port", 0);
            String username = MapTool.get(params, "username", "").toString();
            String password = MapTool.get(params, "password", "").toString();

            if (StringTool.ok(name)) {
                ServerManMachine serverManMachine = new ServerManMachine();
                serverManMachine.setId(R.SnowFlake.nexts());
                serverManMachine.setName(name);
                serverManMachine.setDescription(description);
                serverManMachine.setCreateTime(LocalDateTime.now());
                serverManMachine.setUpdateTime(null);
                serverManMachine.setCreateUserId(CurrentUserManager.getUser().getId());
                serverManMachine.setUpdateUserId(null);
                serverManMachine.setOwnerUserId(CurrentUserManager.getUser().getId());
                serverManMachine.setType(type);
                serverManMachine.setAddress(address);
                serverManMachine.setPort(port);
                serverManMachine.setUsername(username);
                serverManMachine.setPassword(password);
                serverManMachine.setIsDelete(false);

                int flag = serverManMachineMapper.insert(serverManMachine);
                if (flag > 0) {
//                    info.setPassword("*");
                    return ResponseData.okData("serverManMachine", serverManMachine);
                }
            }
        }
        return ResponseData.error("创建失败，请完善信息");
    }

    @PostMapping("edit")
    public ResponseData edit(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id", "name", "description", "type", "address", "port", "username", "password")) {
            String id = MapTool.get(params, "id", "").toString();
            String name = MapTool.get(params, "name", "").toString();
            String description = MapTool.get(params, "description", "").toString();
            String type = MapTool.get(params, "type", "").toString();
            String address = MapTool.get(params, "address", "").toString();
            int port = MapTool.getInt(params, "port", 0);
            String username = MapTool.get(params, "username", "").toString();
            String password = MapTool.get(params, "password", "").toString();

            if (StringTool.ok(id, name)) {
                ServerManMachine serverManMachine = serverManMachineMapper.selectById(id);
                if (serverManMachine != null) {
                    // 非所属者不能编辑
                    if (!serverManMachine.getOwnerUserId().equals(CurrentUserManager.getUser().getId())) {
                        return ResponseData.error("编辑失败，非所属者不能编辑");
                    }
                    serverManMachine.setName(name);
                    serverManMachine.setDescription(description);
                    serverManMachine.setUpdateTime(LocalDateTime.now());
                    serverManMachine.setUpdateUserId(CurrentUserManager.getUser().getId());
                    serverManMachine.setType(type);
                    serverManMachine.setAddress(address);
                    serverManMachine.setPort(port);
                    serverManMachine.setUsername(username);
                    serverManMachine.setPassword(password);

                    int flag = serverManMachineMapper.updateById(serverManMachine);
                    if (flag > 0) {
//                        info.setPassword("*");
                        return ResponseData.okData("serverManMachine", serverManMachine);
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
                ServerManMachine serverManMachine = serverManMachineMapper.selectById(id);
                if (serverManMachine != null) {
                    // 非所属者不能删除
                    if (!serverManMachine.getOwnerUserId().equals(CurrentUserManager.getUser().getId())) {
                        return ResponseData.error("删除失败，非所属者不能删除");
                    }

                    serverManMachine.setIsDelete(true);
                    int flag = serverManMachineMapper.updateById(serverManMachine);
                    if (flag > 0) {
//                        info.setPassword("*");
                        return ResponseData.okData("serverManMachine", serverManMachine);
                    }
                }
            }
        }
        return ResponseData.error("删除失败，请重新选择");
    }

    @PostMapping("list")
    public ResponseData list(@RequestBody Map<String, Object> params) {
        List<ServerManMachine> serverManMachines = serverManMachineMapper.selectList(new LambdaQueryWrapper<ServerManMachine>()
                .eq(ServerManMachine::getOwnerUserId, CurrentUserManager.getUser().getId())
                .eq(ServerManMachine::getIsDelete, false)
                .orderByAsc(ServerManMachine::getName));
        ResponseData responseData = ResponseData.ok();
        responseData.putDataMap("serverManMachines", serverManMachines);
        return responseData;
    }

    @PostMapping("get")
    public ResponseData get(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        if (MapTool.ok(params, "id")) {
            String id = MapTool.get(params, "id", "").toString();
            ServerManMachine serverManMachine = serverManMachineMapper.selectById(id);
            responseData.putDataMap("serverManMachine", serverManMachine);
        }
        return responseData;
    }
}
