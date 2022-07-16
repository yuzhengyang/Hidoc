package com.yuzhyn.hidoc.app.unuse;

import com.yuzhyn.azylee.core.systems.bases.SystemTypeTool;
import com.yuzhyn.hidoc.app.application.model.sys.StreamGobbler;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.utils.LinuxFirewallTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.logs.Alog;

import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Map;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/sh")
public class ShellController {

    @PostMapping("/addFirewall")
    public ResponseData addFirewall(@RequestParam int port) {
        boolean addFlag = LinuxFirewallTool.addPort(port, "tcp");
        boolean rldFlag = LinuxFirewallTool.reload();
        return ResponseData.ok("addFlag: " + addFlag + " ,rldFlag: " + rldFlag);
    }

    @PostMapping("/removeFirewall")
    public ResponseData removeFirewall(@RequestParam int port) {
        boolean addFlag = LinuxFirewallTool.removePort(port, "tcp");
        boolean rldFlag = LinuxFirewallTool.reload();
        return ResponseData.ok("addFlag: " + addFlag + " ,rldFlag: " + rldFlag);
    }

    @PostMapping("/exe")
    public ResponseData exe(@RequestBody Map map) {
        String cmd = MapTool.get(map, "cmd", "");
        if (!SystemTypeTool.isLinux()) {
            Alog.w("暂不支持非Linux的系统");
            return ResponseData.error("暂不支持非Linux的系统");
        }
        try {
            String[] cmdA = {"/bin/sh", "-c", cmd};
            Process process = Runtime.getRuntime().exec(cmdA);
            LineNumberReader br = new LineNumberReader(new InputStreamReader(
                    process.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                sb.append(line).append("\n");
            }
            Alog.i(sb.toString());
            return ResponseData.ok(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseData.error();
    }

    @PostMapping("/at")
    public ResponseData at() {
        try {
            String homeDirectory = System.getProperty("user.home");
            Process process = Runtime.getRuntime().exec(String.format("sh -c ls %s", homeDirectory));
            StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
            Executors.newSingleThreadExecutor().submit(streamGobbler);
            int exitCode = process.waitFor();
            assert exitCode == 0;
        } catch (Exception ex) {
        }

        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("sh", "-c", "ls");
            builder.directory(new File(System.getProperty("user.home")));
            Process process = builder.start();
            StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
            Executors.newSingleThreadExecutor().submit(streamGobbler);
            int exitCode = process.waitFor();
            assert exitCode == 0;
        } catch (Exception ex) {
        }
        return ResponseData.ok("运行完成");
    }
}
