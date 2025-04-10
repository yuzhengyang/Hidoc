//package com.yuzhyn.hidoc.app.http;
//
//import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
//import com.yuzhyn.azylee.core.ios.dirs.DirTool;
//import com.yuzhyn.hidoc.app.aarg.R;
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.concurrent.TimeUnit;
//
//public class DownloadFile {
//    public static void main(String[] args) throws IOException {
//        String url = "https://file.uhsea.com/2503/e5ef51d7af473020ec2dabffbd733b004A.udp";
//        String uuid = UUIDTool.get();
//        String filename = DirTool.combine("D:\\temp\\20250408", uuid);
//        downloadFile(url, filename);
//        System.out.println("下载完成......");
//    }
//
//
//
//    public static void downloadFile(String url, String filePath) throws IOException {
//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .build();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                throw new IOException("下载失败，响应码: " + response.code());
//            }
//
//            long contentLength = response.body().contentLength();
//            InputStream inputStream = response.body().byteStream();
//            FileOutputStream outputStream = new FileOutputStream(new File(filePath));
//
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            long totalBytesRead = 0;
//            long startTime = System.currentTimeMillis();
//
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//                totalBytesRead += bytesRead;
//                int progress = (int) ((totalBytesRead * 100) / contentLength);
//                long elapsedTime = System.currentTimeMillis () - startTime;
//                if (elapsedTime > 0) {
//                    double speed = (double) totalBytesRead / (elapsedTime / 1000.0) / (1024 * 1024);
//                    System.out.printf ("\r 下载进度: % d%%，下载速度: %.2f MB/s", progress, speed);
//                }
//            }
//
//            System.out.println("\n下载完成");
//        }
//    }
//}
