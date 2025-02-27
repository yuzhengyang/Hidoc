package com.yuzhyn.hidoc.app.utils;

import com.yuzhyn.azylee.core.datas.encrypts.Md5Tool;
import com.yuzhyn.azylee.core.datas.encrypts.MixdeTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * TOTP Generator
 * 微软使用的 TOTP（Time-Based One-Time Password）算法遵循 RFC6238 标准，根据标准支持生成用于双因素登录的验证码
 */
public class TOTPGenerator {
    private static final String ALGORITHM = "HmacSHA1";


    /**
     * 创建返回一个64为的密钥
     * @param strings
     * @return
     */
    public static String generateSecret(String... strings) {
        if (strings.length > 0) {
            String combined = "";
            for (String string : strings){
                if(StringTool.ok(string)) combined += Md5Tool.encrypt(string);
            }
            String secret =  MixdeTool.md5Mix(combined);

            // 将 secret 字符串转换为字节数组
            byte[] secretBytes = secret.getBytes();

            // 创建 Base32 编码器实例
            Base32 base32 = new Base32();
            // 对字节数组进行 Base32 编码
            String base32Encoded = base32.encodeToString(secretBytes);

            System.out.println("原始 secret: " + secret);
            System.out.println("Base32 编码后的字符串: " + base32Encoded);
            return base32Encoded;

        }
        return "undefined";
    }

    /**
     * 生成 TOTP 的 URI
     *
     * @param appName  应用名称
     * @param userName 用户名称
     * @param secret   密钥
     * @param digits   验证码位数
     * @param timeStep 有效时间
     * @return
     */
    public static String generateUri(String appName, String userName, String secret, int digits, int timeStep) {
        // 构建 URI：按照 “otpauth://totp/{0}:{1}?secret={2}&issuer={0}&algorithm=SHA1&digits=6&period=30” 的格式构建令牌 URI
        // 内容包括：组织域名、用户名、密钥等参数填入。
        // 如：otpauth://totp/HIDOC文档:于正洋?secret=thepasswordstring&issuer=HIDOC文档&algorithm=SHA1&digits=6&period=30
        return "otpauth://totp/" + appName + ":" + userName + "?secret=" + secret + "&issuer=" + appName + "&algorithm=SHA1&digits=" + digits + "&period=" + timeStep;
    }

    /**
     * 生成 TOTP 验证码
     *
     * @param secret
     * @param timeStep
     * @param digits
     * @return
     */
    public static String generateNumber(String secret, int timeStep, int digits) {
        try {
            long counter = (System.currentTimeMillis() / 1000) / timeStep;
            byte[] key = new Base32().decode(secret);

            SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(secretKey);

            byte[] counterBytes = new byte[8];
            for (int i = 0; i < 8; i++) {
                counterBytes[7 - i] = (byte) (counter >> (8 * i));
            }

            byte[] hash = mac.doFinal(counterBytes);
            int offset = hash[hash.length - 1] & 0x0F;
            int binary = ((hash[offset] & 0x7F) << 24 | (hash[offset + 1] & 0xFF) << 16 | (hash[offset + 2] & 0xFF) << 8 | (hash[offset + 3] & 0xFF));

            int otp = binary % (int) Math.pow(10, digits);
            return String.format("%0" + digits + "d", otp);
        } catch (Exception ex) {
            return "";
        }
    }

}
