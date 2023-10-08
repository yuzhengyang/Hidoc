package com.yuzhyn.hidoc.app.application.controller4openapi;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.datacoll.DataColl;
import com.yuzhyn.hidoc.app.application.entity.datacoll.DataCollPlan;
import com.yuzhyn.hidoc.app.application.entity.softsup.SoftSupBase;
import com.yuzhyn.hidoc.app.application.entity.softsup.SoftSupClient;
import com.yuzhyn.hidoc.app.application.entity.softsup.SoftSupLimit;
import com.yuzhyn.hidoc.app.application.entity.softsup.SoftSupVersion;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("openapi/softsup")
public class SoftSupApiController {

    @PostMapping("reg")
    public ResponseData create(@RequestBody Map<String, Object> params) {
        // 准备数据，检查数据
        LocalDateTime now = LocalDateTime.now();
        if (!MapTool.ok(params, "id", "token")) return ResponseData.error("创建失败，提交的信息不完整");
        String id = MapTool.get(params, "id", "").toString();
        String token = MapTool.get(params, "token", "").toString();

        // 查询软件基础信息
        SoftSupBase softSupBase = new SoftSupBase();
        softSupBase = softSupBase.selectOne(new LambdaQueryWrapper<SoftSupBase>().eq(SoftSupBase::getToken, token));
        if (softSupBase == null) return ResponseData.error("软件基本信息未定义");

        // 查询软件限制信息，校验限制信息
        boolean isLimitAccess = true;
        String forbidLimitId = "";
        List<SoftSupLimit> limits = new SoftSupLimit().selectList(new LambdaQueryWrapper<SoftSupLimit>().eq(SoftSupLimit::getBaseId, softSupBase.getCreateUserId()));
        if (ListTool.ok(limits)) {

        }

        // 查询客户端登记信息，没有则新增，有则更新
        SoftSupClient client = new SoftSupClient().selectById(id);
        if (client == null) {
            client = new SoftSupClient();
            client.setId(id);
            client.setBaseId(softSupBase.getId());
            client.setClientType(MapTool.getString(params, "ClientType", ""));
            client.setName(MapTool.getString(params, "Name", ""));
            client.setEmail(MapTool.getString(params, "Email", ""));
            client.setIp(MapTool.getString(params, "Ip", ""));
            client.setIpLocation(MapTool.getString(params, "IpLocation", ""));
            client.setMapLocation(MapTool.getString(params, "MapLocation", ""));
            client.setVersion(MapTool.getString(params, "Version", ""));
            client.setIsLimitAccess(isLimitAccess);
            client.setForbidLimitId(forbidLimitId);
            client.setFreeTime(MapTool.getLong(params, "FreeTime", 0));
            client.setWorkTime(MapTool.getLong(params, "WorkTime", 0));
            client.setFreeTimePer(MapTool.getLong(params, "FreeTimePer", 0));
            client.setWorkTimePer(MapTool.getLong(params, "WorkTimePer", 0));
            client.setCreateTime(now);
            client.setUpdateTime(now);
            client.insert();
        } else {
            client.setClientType(MapTool.getString(params, "ClientType", ""));
            client.setName(MapTool.getString(params, "Name", ""));
            client.setEmail(MapTool.getString(params, "Email", ""));
            client.setIp(MapTool.getString(params, "Ip", ""));
            client.setIpLocation(MapTool.getString(params, "IpLocation", ""));
            client.setMapLocation(MapTool.getString(params, "MapLocation", ""));
            client.setVersion(MapTool.getString(params, "Version", ""));
            client.setIsLimitAccess(isLimitAccess);
            client.setForbidLimitId(forbidLimitId);
            client.setFreeTime(MapTool.getLong(params, "FreeTime", 0));
            client.setWorkTime(MapTool.getLong(params, "WorkTime", 0));
            client.setFreeTimePer(MapTool.getLong(params, "FreeTimePer", 0));
            client.setWorkTimePer(MapTool.getLong(params, "WorkTimePer", 0));
            client.setUpdateTime(now);
            client.updateById();
        }


        // 查询软件版本信息
        LambdaQueryWrapper<SoftSupVersion> versionWrapper = new LambdaQueryWrapper<>();
        if (StringTool.ok(client.getLockVersion())){

        }

    }
}