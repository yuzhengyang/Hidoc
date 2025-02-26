package com.yuzhyn.hidoc.app.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 这里暂时不启用，使用前端来生成和展示
 */
public class ZxingTool {
    public static void main(String s) throws WriterException {
        // 使用ZXing生成二维码图片
        int width = 300;
        int height = 300;
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(s, BarcodeFormat.QR_CODE, width, height, hints);
        File outputFile = new File("D:\\temp\\qr_code.png");
//        MatrixToImageWriter.writeToFile(bitMatrix, "png", outputFile);
    }
}
