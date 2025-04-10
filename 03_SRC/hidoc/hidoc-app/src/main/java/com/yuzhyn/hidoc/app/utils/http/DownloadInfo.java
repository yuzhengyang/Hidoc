package com.yuzhyn.hidoc.app.utils.http;

import java.time.LocalTime;

public class DownloadInfo {
    int progress;
    double speed;
    long remainingSeconds;

    DownloadInfo(int progress, double speed, long remainingSeconds) {
        this.progress = progress;
        this.speed = speed;
        this.remainingSeconds = remainingSeconds;
    }
}
