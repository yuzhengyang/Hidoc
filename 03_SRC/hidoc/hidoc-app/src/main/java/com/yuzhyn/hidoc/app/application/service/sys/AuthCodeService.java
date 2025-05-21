package com.yuzhyn.hidoc.app.application.service.sys;

import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;


@Service
public class AuthCodeService {

    @Autowired
    SysLockService sysLockService;

    /**
     * 创建验证码
     *
     * @param email
     * @return 唯一ID，验证码
     * @throws Exception
     */
    public Tuple2<String, String> createCode(String email) throws Exception {
        String uid = R.SnowFlake.nexts();
        String code = UUIDTool.get().substring(0, 6);
        String key = "AuthCodeService" + ":" + email;
        String val = uid + ":" + code;

        if (sysLockService.check(key)) {
            throw new Exception("请求过于频繁，请间隔60秒再试");
        }

        if (sysLockService.lock(key, val, 60L, "")) {
            return Tuples.of(uid, code);
        } else {
            throw new Exception("创建验证码失败");
        }
    }

    public Tuple2<Boolean, String> checkCode(String email, String uid, String code) {
        try {
            if (!StringTool.ok(email, uid, code)) {
                return Tuples.of(false, "请先获取验证码");
            }
            String key = "AuthCodeService" + ":" + email;
            String val = uid + ":" + code;
            if (sysLockService.check(key, val)) {
                return Tuples.of(true, "");
            } else {
                return Tuples.of(false, "验证码不正确或已超时");
            }
        } catch (Exception ex) {
        }
        return Tuples.of(false, "验证码错误");
    }

}