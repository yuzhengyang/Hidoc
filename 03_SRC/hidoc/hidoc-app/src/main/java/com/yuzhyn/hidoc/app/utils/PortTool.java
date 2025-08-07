//package com.yuzhyn.hidoc.app.utils;
//
//import java.io.IOException;
//import java.net.Socket;
//
///**
// * 已转移到azylee类库
// */
//public class PortTool {
//    public static boolean isOpen(String host, int port) {
//        try (Socket socket = new Socket()) {
//            // 尝试连接到指定主机和端口，超时时间设置为 2000 毫秒
//            socket.connect(new java.net.InetSocketAddress(host, port), 2000);
//            return true;
//        } catch (IOException e) {
//            return false;
//        }
//    }
//}
