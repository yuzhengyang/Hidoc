package com.yuzhyn.hidoc.app.application.controller4openapi;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.exceptions.ExceptionTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.limit.LimitAccess;
import com.yuzhyn.hidoc.app.application.entity.limit.LimitAccessLog;
import com.yuzhyn.hidoc.app.application.mapper.limit.LimitAccessLogMapper;
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
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("openapi/limit")
public class LimitApiController {

    @Autowired
    LimitAccessMapper limitAccessMapper;

    @Autowired
    LimitAccessLogMapper limitAccessLogMapper;

    @PostMapping("access")
    public ResponseData access(@RequestBody Map<String, Object> params) {
        if (!MapTool.ok(params, "id", "token")) return new ResponseData(19001, "请求缺少关键参数，如：id、token");

        String id = MapTool.get(params, "id", "").toString();
        String token = MapTool.get(params, "token", "").toString();
        LimitAccess acc = limitAccessMapper.selectById(id);

        if (acc == null) return new ResponseData(19002, "没有对应的限制规则");
        if (!acc.getToken().equals(token)) return new ResponseData(19003, "限制规则token认证失败");
        if (!acc.getIsEnable()) return new ResponseData(18001, "限制规则未启用");
        if (acc.getIsDelete()) return new ResponseData(18002, "限制规则已删除");

        Tuple3<Boolean, Integer, String> result = doAccess(acc, params);
        String hour = MapTool.get(params, "hour", "").toString();
        String position = MapTool.get(params, "position", "").toString();
        String email = MapTool.get(params, "email", "").toString();
        String mac = MapTool.get(params, "mac", "").toString();
        String address = MapTool.get(params, "address", "").toString();
        String version = MapTool.get(params, "version", "").toString();
        String account = MapTool.get(params, "account", "").toString();
        String machine = MapTool.get(params, "machine", "").toString();

        LimitAccessLog limitAccessLog = new LimitAccessLog();
        limitAccessLog.setId(R.SnowFlake.nexts());
        limitAccessLog.setLimitAccessId(id);
        limitAccessLog.setHour(hour);
        limitAccessLog.setPosition(position);
        limitAccessLog.setEmail(email);
        limitAccessLog.setMac(mac);
        limitAccessLog.setAddress(address);
        limitAccessLog.setVersion(version);
        limitAccessLog.setAccount(account);
        limitAccessLog.setMachine(machine);
        limitAccessLog.setIsAccess(result.getT1());
        limitAccessLog.setMessage(result.getT3());
        limitAccessLog.setCreateTime(LocalDateTime.now());
        limitAccessLogMapper.insert(limitAccessLog);

        return new ResponseData(result.getT2(), result.getT3());
    }

    public Tuple3<Boolean, Integer, String> doAccess(LimitAccess acc, Map<String, Object> params) {

        {
            String address = MapTool.get(params, "address", "").toString();
            String msgNotAllowAddress = "您所在的区域不允许使用本软件，请在允许区域内使用该软件";
            // 有允许地址，不在允许地址内，返回禁止
            if (StringTool.ok(acc.getAllowAddresses()) && !have(acc.getAllowAddresses(), address)) return Tuples.of(false, 2001, msgNotAllowAddress);
            // 有禁止地址，在禁止地址内，返回禁止
            if (StringTool.ok(acc.getForbidAddresses()) && have(acc.getForbidAddresses(), address)) return Tuples.of(false, 2002, msgNotAllowAddress);
        }
        {
            String version = MapTool.get(params, "version", "").toString();
            String msgNotAllowVersion = "当前版本已经停止使用，请使用新版本的软件";
            // 有允许版本，不在允许版本内，返回禁止
            if (StringTool.ok(acc.getAllowVersions()) && !have(acc.getAllowVersions(), version)) return Tuples.of(false, 2003, msgNotAllowVersion);
            // 有禁止版本，在禁止版本内，返回禁止
            if (StringTool.ok(acc.getForbidVersions()) && have(acc.getForbidVersions(), version)) return Tuples.of(false, 2004, msgNotAllowVersion);
        }
        {
            String account = MapTool.get(params, "account", "").toString();
            String msgNotAllowAccount = "您的账号已被锁定，请更换账号再试";
            // 有允许地址，不在允许地址内，返回禁止
            if (StringTool.ok(acc.getAllowAccounts()) && !have(acc.getAllowAccounts(), account)) return Tuples.of(false, 2005, msgNotAllowAccount);
            // 有禁止地址，在禁止地址内，返回禁止
            if (StringTool.ok(acc.getForbidAccounts()) && have(acc.getForbidAccounts(), account)) return Tuples.of(false, 2006, msgNotAllowAccount);
        }
        {
            String machine = MapTool.get(params, "machine", "").toString();
            String msgNotAllowMachine = "当前软件在本机已被限制使用";
            // 有允许地址，不在允许地址内，返回禁止
            if (StringTool.ok(acc.getAllowMachines()) && !have(acc.getAllowMachines(), machine)) return Tuples.of(false, 2007, msgNotAllowMachine);
            // 有禁止地址，在禁止地址内，返回禁止
            if (StringTool.ok(acc.getForbidMachines()) && have(acc.getForbidMachines(), machine)) return Tuples.of(false, 2008, msgNotAllowMachine);
        }
        return Tuples.of(true, 1001, "通过");
    }

    private boolean have(String rule, String s) {
        if (StringTool.ok(rule, s)) {
            String[] array = StringTool.split(rule, ",", true, true, true);
            if (ListTool.ok(array)) {
                for (String item : array) {
                    if (item.contains(s)) return true;
                }
            }

        }
        return false;
    }

}
