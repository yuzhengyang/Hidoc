//package com.yuzhyn.hidoc.app.utils;
//
//import com.yuzhyn.azylee.core.logs.Alog;
//import com.yuzhyn.azylee.core.systems.bases.SystemTypeTool;
//
//import java.io.InputStreamReader;
//import java.io.LineNumberReader;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 已经迁移
// */
//public class LinuxShellTool {
//
//    public static List<String> sh(String cmd) {
//        List<String> result = null;
//        if (!SystemTypeTool.isLinux()) {
//            Alog.w("暂不支持非Linux的系统");
//            return result;
//        }
//        try {
//            String[] cmdA = {"/bin/sh", "-c", cmd};
//            Process process = Runtime.getRuntime().exec(cmdA);
//            LineNumberReader br = new LineNumberReader(new InputStreamReader(process.getInputStream()));
//            result = new ArrayList<>();
//            String line;
//            while ((line = br.readLine()) != null) {
//                result.add(line);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//}
