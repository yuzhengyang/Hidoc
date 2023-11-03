package com.yuzhyn.hidoc.app.application.controller4openapi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.application.entity.softsup.SoftSupBase;
import com.yuzhyn.hidoc.app.application.entity.softsup.SoftSupClient;
import com.yuzhyn.hidoc.app.application.entity.softsup.SoftSupLimit;
import com.yuzhyn.hidoc.app.application.entity.softsup.SoftSupVersion;
import com.yuzhyn.hidoc.app.application.mapper.softsup.SoftSupBaseMapper;
import com.yuzhyn.hidoc.app.application.mapper.softsup.SoftSupClientMapper;
import com.yuzhyn.hidoc.app.application.mapper.softsup.SoftSupLimitMapper;
import com.yuzhyn.hidoc.app.application.mapper.softsup.SoftSupVersionMapper;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("openapi/softsup")
public class SoftSupApiController {

    @Autowired
    SoftSupBaseMapper softSupBaseMapper;
    @Autowired
    SoftSupClientMapper softSupClientMapper;
    @Autowired
    SoftSupLimitMapper softSupLimitMapper;
    @Autowired
    SoftSupVersionMapper softSupVersionMapper;

    @PostMapping("reg")
    public ResponseData create(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        List<String> step = new ArrayList<>();
        // 准备数据，检查数据
        LocalDateTime now = LocalDateTime.now();
        String id = MapTool.get(params, "id", "").toString();
        String token = MapTool.get(params, "token", "").toString();
        if (!StringTool.ok(id)) return ResponseData.error("创建失败，提交的唯一主键不能为空");
        if (!StringTool.ok(token)) return ResponseData.error("创建失败，提交的token不能为空");

        // 查询软件基础信息
        SoftSupBase softSupBase = softSupBaseMapper.selectOne(new LambdaQueryWrapper<SoftSupBase>().eq(SoftSupBase::getToken, token));
        if (softSupBase == null) return ResponseData.error("软件基本信息未定义");

        // 查询软件限制信息，校验限制信息
        boolean isLimitAccess = true;
        String forbidLimitId = "";
        String forbidMessage = "";
        List<SoftSupLimit> limits = softSupLimitMapper.selectList(new LambdaQueryWrapper<SoftSupLimit>().eq(SoftSupLimit::getBaseId, softSupBase.getId()));
        if (ListTool.ok(limits)) {
            for (SoftSupLimit item : limits) {
                switch (item.getLimitType()) {
                    case "FORBID_ID" -> {
                        if (item.getLimitContent().contains("[" + id + "]")) {
                            isLimitAccess = false;
                            forbidLimitId = item.getId();
                            forbidMessage = "限制ID";
                        }
                    }
                    default -> {
                        step.add("LIMIT: default");
                    }
                }
                if (!isLimitAccess) break;
            }
        }
        Map<String, Object> limit = new HashMap<>();
        limit.put("access", isLimitAccess);
        limit.put("id", forbidLimitId);
        limit.put("message", forbidMessage);
        responseData.putDataMap("limit", limit);

        // 查询客户端登记信息，没有则新增，有则更新
        SoftSupClient client = softSupClientMapper.selectById(id);
        if (client == null) {
            client = new SoftSupClient();
            client.setId(id);
            client.setBaseId(softSupBase.getId());
            client.setMachine(MapTool.getString(params, "machine", ""));
            client.setAccount(MapTool.getString(params, "account", ""));
            client.setClientType(MapTool.getString(params, "clientType", ""));
            client.setIp(MapTool.getString(params, "ip", ""));
            client.setIpLocation(MapTool.getString(params, "ipLocation", ""));
            client.setMapLocation(MapTool.getString(params, "mapLocation", ""));
            client.setVersion(MapTool.getString(params, "version", ""));
            client.setIsLimitAccess(isLimitAccess);
            client.setForbidLimitId(forbidLimitId);
            client.setFreeTime(MapTool.getLong(params, "freeTime", 0));
            client.setWorkTime(MapTool.getLong(params, "workTime", 0));
            client.setFreeTimePer(MapTool.getLong(params, "freeTimePer", 0));
            client.setWorkTimePer(MapTool.getLong(params, "workTimePer", 0));
            client.setCreateTime(now);
            client.setUpdateTime(now);
            softSupClientMapper.insert(client);
        } else {
            client.setMachine(MapTool.getString(params, "machine", ""));
            client.setAccount(MapTool.getString(params, "account", ""));
            client.setClientType(MapTool.getString(params, "clientType", ""));
            client.setIp(MapTool.getString(params, "ip", ""));
            client.setIpLocation(MapTool.getString(params, "ipLocation", ""));
            client.setMapLocation(MapTool.getString(params, "mapLocation", ""));
            client.setVersion(MapTool.getString(params, "version", ""));
            client.setIsLimitAccess(isLimitAccess);
            client.setForbidLimitId(forbidLimitId);
            client.setFreeTime(MapTool.getLong(params, "freeTime", 0));
            client.setWorkTime(MapTool.getLong(params, "workTime", 0));
            client.setFreeTimePer(MapTool.getLong(params, "freeTimePer", 0));
            client.setWorkTimePer(MapTool.getLong(params, "workTimePer", 0));
            client.setUpdateTime(now);
            softSupClientMapper.updateById(client);
        }

        // 查询软件版本信息
        LambdaQueryWrapper<SoftSupVersion> versionWrapper = new LambdaQueryWrapper<>();
        if (StringTool.ok(client.getLockVersion())) {
            versionWrapper.eq(SoftSupVersion::getVersion, client.getLockVersion());
        }
        versionWrapper.orderByDesc(SoftSupVersion::getNum);
        Page<SoftSupVersion> page = new Page<>(1, 1);
        IPage<SoftSupVersion> versions = softSupVersionMapper.selectPage(page, versionWrapper);
        if (ListTool.ok(versions.getRecords())) {
            responseData.putDataMap("version", versions.getRecords().get(0));
        }
        responseData.putDataMap("step", step);
        return responseData;
    }
}