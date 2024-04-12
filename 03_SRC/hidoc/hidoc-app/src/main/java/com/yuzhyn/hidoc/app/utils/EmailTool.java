package com.yuzhyn.hidoc.app.utils;


import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.objects.ObjectTool;
import jakarta.mail.Address;
import jakarta.mail.Authenticator;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import reactor.util.function.Tuple2;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 邮箱工具
 * 使用步骤：
 * 1、new对象
 * 2、createSession
 * 3、sendMessage
 */
public class EmailTool {

    private String personalName;
    private String emailAddress;
    private String emailPassword;
    private Session session;
    private Integer port;
    private boolean isSsl;
    private String smtpHost;
    private boolean isDebug = false;
    private String charset = "UTF-8";
    private String contentType = "text/html;charset=UTF-8";

    /**
     * 初始化邮件工具
     * @param address 发件箱地址
     * @param personal 发件人姓名
     * @param password 密码/授权码
     * @param port 端口号
     * @param isSsl 是否SSL
     * @param smtpHost SMTP服务器地址
     */
    public EmailTool(String address, String personal, String password, int port, boolean isSsl, String smtpHost) {
        this.emailAddress = address;
        this.personalName = personal;
        this.emailPassword = password;
        this.port = port;
        this.isSsl = isSsl;
        this.smtpHost = smtpHost;
    }


    public void createSession() {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", this.smtpHost);
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.port", String.valueOf(port));
        props.setProperty("mail.user", this.emailAddress);
        props.setProperty("mail.password", emailPassword);
//        props.setProperty("mail.smtp.socketFactory.port",String.valueOf(port));

        if (this.isSsl) {
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.starttls.enable", true);
            props.put("mail.smtp.ssl.trust", true);
        }

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };

        session = Session.getInstance(props, authenticator);
        session.setDebug(this.isDebug);
    }

    public Address[] createAddressByT2(List<Tuple2<String, String>> datas) throws UnsupportedEncodingException {
        if (ListTool.ok(datas)) {
            Address[] addresses = new Address[datas.size()];
            for (int i = 0; i < datas.size(); i++) {
                addresses[i] = new InternetAddress(
                        ObjectTool.optional(datas.get(i).getT1(), ""),
                        ObjectTool.optional(datas.get(i).getT2(), ""), this.charset);
            }
            return addresses;
        }
        return null;
    }

    public Address[] createAddress(List<String> datas) throws AddressException {
        if (ListTool.ok(datas)) {
            Address[] addresses = new Address[datas.size()];
            for (int i = 0; i < datas.size(); i++) {
                addresses[i] = new InternetAddress(datas.get(i));
            }
            return addresses;
        }
        return null;
    }

    public void sendMessage(Address[] recipients, String title, String content) throws Exception {
        MimeMessage message = createMessage(session, recipients, title, content);
        Transport transport = session.getTransport();
        transport.connect(this.emailAddress, this.emailPassword);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    public void sendMessage(MimeMessage message) throws MessagingException {
        Transport transport = session.getTransport();
        transport.connect(this.emailAddress, this.emailPassword);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }


//    private String getEmailSmtp() {
//        if (emailAddress.endsWith("@163.com")) return "smtp.163.com";
//        if (emailAddress.endsWith("@126.com")) return "smtp.126.com";
//        return "";
//    }

    private MimeMessage createMessage(Session session, Address[] recipients, String title, String content) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(this.emailAddress, this.personalName, this.charset));
        message.setRecipients(MimeMessage.RecipientType.TO, recipients);
        message.setSubject(title, this.charset);
        message.setContent(content, this.contentType);
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }
}