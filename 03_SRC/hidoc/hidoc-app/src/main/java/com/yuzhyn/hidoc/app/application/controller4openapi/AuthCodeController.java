package com.yuzhyn.hidoc.app.application.controller4openapi;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.exceptions.ExceptionTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.service.sys.AuthCodeService;
import com.yuzhyn.hidoc.app.application.service.sys.EmailService;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.util.function.Tuple2;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("openapi/authcode")
public class AuthCodeController {

    @Autowired
    SysUserLiteMapper sysUserLiteMapper;

    @Autowired
    AuthCodeService authCodeService;

    @Autowired
    EmailService emailService;

    @PostMapping("getForRegister")
    public ResponseData getForRegister(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "email")) {
            String email = MapTool.get(params, "email", "").toString();

            if (!StringTool.ok(email)) {
                return ResponseData.error("请输入邮箱");
            }

            String limitEmail = MapTool.getString(R.Maps.registerConfig, "limit-email", "");
            if (!email.endsWith(limitEmail)) {
                return ResponseData.error("系统限制仅支持：" + limitEmail + "的邮箱注册账号");
            }

            try {
                Tuple2<String, String> codeInfo = authCodeService.createCode(email);
                emailService.sendAuthCode(email, codeInfo.getT2());
                return ResponseData.okData("uid", codeInfo.getT1());

            } catch (Exception ex) {
                log.error(ExceptionTool.getStackTrace(ex));
                return ResponseData.error("获取验证码异常：" + ex.getMessage());
            }
        }
        return ResponseData.error("请输入邮箱");
    }

    @PostMapping("getForResetPassword")
    public ResponseData getAuthCodeForResetPassword(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "email")) {
            String email = MapTool.get(params, "email", "").toString();

            if (!StringTool.ok(email)) {
                return ResponseData.error("请输入邮箱");
            }

            try {
                SysUserLite sysUserLite = sysUserLiteMapper.selectOne(new LambdaQueryWrapper<SysUserLite>().eq(SysUserLite::getEmail, email));
                if (sysUserLite != null) {
                    Tuple2<String, String> codeInfo = authCodeService.createCode(email);
                    emailService.sendAuthCodeForResetPassword(email, sysUserLite.getName(), codeInfo.getT2());
                    return ResponseData.okData("uid", codeInfo.getT1());
                }else{
                    return ResponseData.error("未找到邮箱账号");
                }

            } catch (Exception ex) {
                log.error(ExceptionTool.getStackTrace(ex));
                return ResponseData.error("获取验证码异常：" + ex.getMessage());
            }
        }
        return ResponseData.error("请输入邮箱");
    }
}