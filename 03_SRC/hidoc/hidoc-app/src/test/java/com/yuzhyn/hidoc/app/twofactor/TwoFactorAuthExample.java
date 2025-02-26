package com.yuzhyn.hidoc.app.twofactor;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TwoFactorAuthExample {
    public static void main(String[] args) throws Exception {
        // 创建GoogleAuthenticator实例
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        // 生成密钥
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        String secretKey = key.getKey();

        // 生成二维码URL getOtpAuthUrl
        String user = "aaaaaaa";
        String issuer = "aaaaaaa";
        String qrUrl = GoogleAuthenticatorQRGenerator.getOtpAuthURL(issuer, user, key);

        // 使用ZXing生成二维码图片
        int width = 300;
        int height = 300;
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(qrUrl, BarcodeFormat.QR_CODE, width, height, hints);
        File outputFile = new File("D:\\temp\\qr_code.png");
        MatrixToImageWriter.writeToFile(bitMatrix, "png", outputFile);

        // 模拟用户输入的验证码
        String userEnteredCode = "078306";
        // 验证验证码
        boolean isCodeValid = gAuth.authorize(secretKey, Integer.parseInt(userEnteredCode));
        if (isCodeValid) {
            System.out.println("验证码验证成功");
        } else {
            System.out.println("验证码验证失败");
        }
    }
}