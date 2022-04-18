package com.yuzhyn.hidoc.app.application.service.sys;

import com.yuzhyn.azylee.ext.web.mails.EmailTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import java.util.Arrays;

@Service
public class EmailService {

    @Value("${email.address}")
    private String address;

    @Value("${email.personal}")
    private String personal;

    @Value("${email.password}")
    private String password;

    @Value("${email.port}")
    private int port;

    @Value("${email.isSsl}")
    private boolean isSsl;

    @Value("${email.smtpHost}")
    private String smtpHost;

    public void sendAuthCode(String email, String code) throws Exception {
        EmailTool emailTool = new EmailTool(address, personal, password, port, isSsl, smtpHost);
        emailTool.createSession();
        Address[] addresses = emailTool.createAddress(Arrays.asList(email));
        emailTool.sendMessage(addresses, "注册验证码", "您的注册验证码为：" + code);
    }
}
