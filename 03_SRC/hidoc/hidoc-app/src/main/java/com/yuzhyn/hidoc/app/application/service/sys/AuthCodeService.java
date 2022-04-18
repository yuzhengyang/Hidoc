package com.yuzhyn.hidoc.app.application.service.sys;

import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import org.springframework.stereotype.Service;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;


@Service
public class AuthCodeService {

    /**
     * 创建验证码
     *
     * @param email
     * @return 唯一ID，验证码
     * @throws Exception
     */
    public Tuple2<String, String> createCode(String email) throws Exception {
        String uid = R.SnowFlake.nexts();
        String code = UUIDTool.get();

        Tuple3<String, String, Long> codeInfoHis = R.Map.AuthCode.get(email);
        if (codeInfoHis != null) {
            long waitTime = codeInfoHis.getT3() - (System.currentTimeMillis() - (60 * 1000));
            if (waitTime > 0) {
                throw new Exception("请求过于频繁，请稍后再试，请等待：" + waitTime / 1000 + "秒");
            }
        }

        if (StringTool.ok(uid, code)) {
            code = code.substring(0, 6);
            Tuple3<String, String, Long> codeInfo = Tuples.of(uid, code, System.currentTimeMillis());
            R.Map.AuthCode.put(email, codeInfo);
            return Tuples.of(uid, code);
        } else {
            throw new Exception("code gen failed!");
        }
    }

    public Tuple2<Boolean, String> checkCode(String email, String uid, String code) {
        Tuple3<String, String, Long> codeInfo = R.Map.AuthCode.get(email);
        if (codeInfo == null) return Tuples.of(false, "请先获取验证码");

        try {
            if (codeInfo.getT1().equals(uid) && codeInfo.getT2().equals(code)) {
                return Tuples.of(true, "");
            }
        } catch (Exception ex) {
        }
        return Tuples.of(false, "验证码错误");
    }

}