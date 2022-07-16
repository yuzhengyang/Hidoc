package com.yuzhyn.hidoc.app.application.service.sys;

import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.ext.web.mails.EmailTool;
import com.yuzhyn.hidoc.app.aarg.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import java.util.Arrays;

@Service
public class EmailService {

    public EmailTool createEmailTool() {

        String address = MapTool.getString(R.Maps.emailConfig, "address", "");
        String personal = MapTool.getString(R.Maps.emailConfig, "personal", "");
        String password = MapTool.getString(R.Maps.emailConfig, "password", "");
        int port = MapTool.getInt(R.Maps.emailConfig, "port", "");
        boolean isSsl = MapTool.getBoolean(R.Maps.emailConfig, "is-ssl", "");
        String smtpHost = MapTool.getString(R.Maps.emailConfig, "smtp-host", "");

        EmailTool emailTool = new EmailTool(address, personal, password, port, isSsl, smtpHost);
        return emailTool;
    }

    public void sendAuthCode(String email, String code) throws Exception {
        EmailTool emailTool = createEmailTool();
        emailTool.createSession();
        Address[] addresses = emailTool.createAddress(Arrays.asList(email));
        emailTool.sendMessage(addresses, "注册验证码", "您的注册验证码为：" + code);
    }

    public void sendAuthCodeForResetPassword(String email, String username, String code) throws Exception {
        EmailTool emailTool = createEmailTool();
        emailTool.createSession();
        Address[] addresses = emailTool.createAddress(Arrays.asList(email));
        emailTool.sendMessage(addresses, "注册验证码",
                "您好： " + username + " ， 您的验证码为： " + code + "");
    }
}
