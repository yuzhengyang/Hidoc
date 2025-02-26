package com.yuzhyn.hidoc.app.utils;

import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * TOTP Generator
 * 微软使用的 TOTP（Time-Based One-Time Password）算法遵循 RFC6238 标准，根据标准支持生成用于双因素登录的验证码
 */
public class TOTPGenerator {
    private static final String ALGORITHM = "HmacSHA1";

    public static String generate(String secret, int timeStep, int digits) {
        try {
            return generateTOTP(secret, timeStep, digits);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String uri(String orgName, String userName, String secret, int digits, int timeStep) {
        // 构建 URI：按照 “otpauth://totp/{0}:{1}?secret={2}&issuer={0}&algorithm=SHA1&digits=6&period=30” 的格式构建令牌 URI
        // 内容包括：组织域名、用户名、密钥等参数填入。
        // 如：otpauth://totp/HIDOC文档:于正洋?secret=thepasswordstring&issuer=HIDOC文档&algorithm=SHA1&digits=6&period=30
        return "otpauth://totp/" + orgName + ":" + userName + "?secret=" + secret + "&issuer=" + orgName + "&algorithm=SHA1&digits=" + digits + "&period=" + timeStep;
    }

    private static String generateTOTP(String secret, int timeStep, int digits) throws Exception {
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
    }

}
