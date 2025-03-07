package com.yuzhyn.hidoc.app.application.service.sys;

import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.utils.EmailTool;
import jakarta.mail.Address;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class EmailService {

    public EmailTool createEmailTool() {

        String address = MapTool.getString(R.Maps.emailConfig, "address", "");
        String personal = MapTool.getString(R.Maps.emailConfig, "personal", "");
        String password = MapTool.getString(R.Maps.emailConfig, "password", "");
        int port = MapTool.getInt(R.Maps.emailConfig, "port", "465");
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

    public boolean sendStartServerMessage(String email) throws Exception {
        try {
            EmailTool emailTool = createEmailTool();
            emailTool.createSession();
            Address[] addresses = emailTool.createAddress(Arrays.asList(email));
            emailTool.sendMessage(addresses,
                    "HIDOC-服务启动通知",
                    "服务正在启动，请关注服务状态（本邮件仅发送给超级管理员）");
            return true;
        } catch (Throwable ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void sendAuthCodeForResetPassword(String email, String username, String code) throws Exception {
        EmailTool emailTool = createEmailTool();
        emailTool.createSession();
        Address[] addresses = emailTool.createAddress(Arrays.asList(email));
        emailTool.sendMessage(addresses, "注册验证码",
                "您好： " + username + " ， 您的验证码为： " + code + "");
    }

    public boolean sendCardUserPassword(String email, String password, String cardName, String levelNames) {
        try {
            EmailTool emailTool = createEmailTool();
            emailTool.createSession();
            Address[] addresses = emailTool.createAddress(Arrays.asList(email));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("您好，欢迎使用 " + cardName + "<br />");
            stringBuilder.append("账号：" + email + "<br />");
            stringBuilder.append("密码：" + password + "<br />");
            stringBuilder.append("用户等级：" + levelNames + "<br />");
            stringBuilder.append("<br />");
            stringBuilder.append("<hr />");
            stringBuilder.append("本邮件由系统自动发出，请勿回复。"  + "<br />");
            stringBuilder.append("谢谢！"  + "<br />");
            emailTool.sendMessage(addresses,
                    cardName + "-账号注册",
                    stringBuilder.toString());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
