package com.yuzhyn.hidoc.app.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class EmailTest {
    public static void main(String[] args) {
        // 配置邮件属性
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.163.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl", true);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // 创建邮件会话
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("hi_notice@163.com", "BVTFAVZTCAARBXEQ");
            }
        });

        try {
            // 创建邮件消息
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("hi_notice@163.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("yuzhyn@163.com"));
            message.setSubject("Test Email");
            message.setText("This is a test email sent from Jakarta Mail.");

            // 发送邮件
            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}