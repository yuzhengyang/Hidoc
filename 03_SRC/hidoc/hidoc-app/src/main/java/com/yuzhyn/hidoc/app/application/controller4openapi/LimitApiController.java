package com.yuzhyn.hidoc.app.application.controller4openapi;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.exceptions.ExceptionTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.application.entity.limit.LimitAccess;
import com.yuzhyn.hidoc.app.application.mapper.limit.LimitAccessMapper;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.system.ibatis.handler.JsonbTypeHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.util.function.Tuple2;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("openapi/limit")
public class LimitApiController {

    @Autowired
    LimitAccessMapper limitAccessMapper;

    @PostMapping("access")
    public ResponseData access(@RequestBody Map<String, Object> params) {
        if (!MapTool.ok(params, "id", "token")) return new ResponseData(9001, "请求缺少关键参数，如：id、token");

        String id = MapTool.get(params, "id", "").toString();
        String token = MapTool.get(params, "token", "").toString();
        LimitAccess acc = limitAccessMapper.selectById(id);

        if (acc == null) return new ResponseData(9002, "没有对应的限制规则");
        if (!acc.getToken().equals(token)) return new ResponseData(9003, "限制规则token认证失败");
        if (!acc.getIsEnable()) return new ResponseData(8001, "限制规则未启用");
        if (acc.getIsDelete()) return new ResponseData(8002, "限制规则已删除");

        String hour = MapTool.get(params, "hour", "").toString();
        String address = MapTool.get(params, "address", "").toString();
        String position = MapTool.get(params, "position", "").toString();
        String account = MapTool.get(params, "account", "").toString();
        String email = MapTool.get(params, "email", "").toString();
        String version = MapTool.get(params, "version", "").toString();
        String mac = MapTool.get(params, "mac", "").toString();

        String msgNotAllowAddress = "您所在的区域不允许使用本软件，请在允许区域内使用该软件";
        // 有允许地址，不在允许地址内，返回禁止
        if (StringTool.ok(acc.getAllowAddresses()) && !have(acc.getAllowAddresses(), address)) return new ResponseData(2001, msgNotAllowAddress);
        // 有禁止地址，在禁止地址内，返回禁止
        if (StringTool.ok(acc.getForbidAddresses()) && have(acc.getForbidAddresses(), address)) return new ResponseData(2002, msgNotAllowAddress);

        String msgNotAllowVersion = "当前版本已经停止使用，请使用新版本的软件";
        // 有允许版本，不在允许版本内，返回禁止
        if (StringTool.ok(acc.getAllowVersions()) && !have(acc.getAllowVersions(), version)) return new ResponseData(2003, msgNotAllowVersion);
        // 有禁止版本，在禁止版本内，返回禁止
        if (StringTool.ok(acc.getForbidVersions()) && have(acc.getForbidVersions(), version)) return new ResponseData(2004, msgNotAllowVersion);
        return new ResponseData(1001, "通过");
    }

    private boolean have(String rule, String s) {
        if (StringTool.ok(rule, s)) {
            String[] array = StringTool.split(rule, ",", true, true);
            if (ListTool.ok(array)) {
                for (String item : array) {
                    if (item.contains(s)) return true;
                }
            }

        }
        return false;
    }

}
