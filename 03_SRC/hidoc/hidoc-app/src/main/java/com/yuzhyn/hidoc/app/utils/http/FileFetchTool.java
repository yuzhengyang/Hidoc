package com.yuzhyn.hidoc.app.utils.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class FileFetchTool {
    public static void download(String url, String filePath, Function<DownloadInfo, Void> callback) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("下载失败，响应码: " + response.code());
            }

            long contentLength = response.body().contentLength();
            InputStream inputStream = response.body().byteStream();
            FileOutputStream outputStream = new FileOutputStream(new File(filePath));

            byte[] buffer = new byte[4096];
            int bytesRead;
            long totalBytesRead = 0;
            long startTime = System.currentTimeMillis();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                int progress = (int) ((totalBytesRead * 100) / contentLength);
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > 0) {
                    double speed = (double) totalBytesRead / (elapsedTime / 1000.0) / 1024;
                    long remainingBytes = contentLength - totalBytesRead;
                    long remainingSeconds = (long) (remainingBytes / (speed * 1024));
                    DownloadInfo info = new DownloadInfo(progress, speed, remainingSeconds);
                    callback.apply(info);
                }
            }
        }
    }
}