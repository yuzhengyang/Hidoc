package com.yuzhyn.hidoc.app.store;

import com.yuzhyn.azylee.core.systems.bases.SystemStatusTool;
import com.yuzhyn.azylee.core.systems.models.MemoryInfo;
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


        MemoryInfo memInfo = SystemStatusTool.getRam();

        StringBuilder sb = new StringBuilder();
        sb.append("ip:  " + ip);
        sb.append("cpuinfo:  " + SystemStatusTool.getCpuUseRatio());

        if (memInfo != null) {
            sb.append("MemTotal:  " + String.valueOf(memInfo.getMemTotal()));
            sb.append("MemFree:  " + String.valueOf(memInfo.getMemFree()));
        }
        return sb.toString();
    }
}
