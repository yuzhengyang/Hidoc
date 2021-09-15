package com.yuzhyn.hidoc.app.application.customization.controller;

import com.yuzhyn.hidoc.app.utils.SystemStatusTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

@Slf4j
@RestController
@RequestMapping("/ls")
public class LinuxStatusController {

    @GetMapping("/ram")
    public String get() {
        String ip = "";
        try {
            InetAddress inet = InetAddress.getLocalHost();
            ip = inet.toString().substring(inet.toString().indexOf("/") + 1);
        } catch (Exception ex) {
        }


        long[] memInfo = SystemStatusTool.getRam();

        StringBuilder sb = new StringBuilder();
        sb.append("ip:  " + ip);
        sb.append("cpuinfo:  " + SystemStatusTool.getCpuUseRatio());

        if (memInfo != null && memInfo.length > 1) {
            sb.append("MemTotal:  " + String.valueOf(memInfo[0]));
            sb.append("MemFree:  " + String.valueOf(memInfo[1]));
        }
        return sb.toString();
    }
}
