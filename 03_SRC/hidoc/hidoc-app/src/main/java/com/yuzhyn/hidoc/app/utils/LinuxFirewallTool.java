//package com.yuzhyn.hidoc.app.utils;
//
//import com.yuzhyn.azylee.core.datas.collections.ListTool;
//import com.yuzhyn.azylee.core.datas.strings.StringTool;
//import com.yuzhyn.azylee.core.systems.linuxs.shell.LinuxShellTool;
//
//import java.util.List;
//
///**
// * 已经迁移
// */
//public class LinuxFirewallTool {
//    /**
//     *# 开启
//     *
//     * # 重启
//     *
//     * # 关闭
//     *
//     */
//
//    /**
//     * 查看防火墙启用状态
//     *
//     * @return
//     */
//    public static boolean state() {
//        String cmd = "firewall-cmd --state";
//        List<String> result = LinuxShellTool.sh(cmd);
//        if (StringTool.itemLike(result, "running") > -1) {
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 查询端口是否开通防火墙
//     *
//     * @param port
//     * @param protocol
//     * @return
//     */
//    public static boolean queryPort(int port, String protocol) {
//        String cmd = "firewall-cmd --query-port=" + port + "/" + protocol;
//        List<String> result = LinuxShellTool.sh(cmd);
//        if (StringTool.itemLike(result, "yes") > -1) {
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 增加端口到防火墙
//     *
//     * @param port
//     * @param protocol
//     * @return
//     */
//    public static boolean addPort(int port, String protocol) {
//        String cmd = "firewall-cmd --permanent --add-port=" + port + "/" + protocol;
//        List<String> result = LinuxShellTool.sh(cmd);
//        if (StringTool.itemLike(result, "success") > -1) {
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 删除端口
//     *
//     * @param port
//     * @param protocol
//     * @return
//     */
//    public static boolean removePort(int port, String protocol) {
//        String cmd = "firewall-cmd --permanent --remove-port=" + port + "/" + protocol;
//        List<String> result = LinuxShellTool.sh(cmd);
//        if (StringTool.itemLike(result, "success") > -1) {
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 重新加载防火墙规则
//     *
//     * @return
//     */
//    public static boolean reload() {
//        String cmd = "firewall-cmd --reload";
//        List<String> result = LinuxShellTool.sh(cmd);
//        if (StringTool.itemLike(result, "success") > -1) {
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 开启防火墙
//     *
//     * @return
//     */
//    public static boolean start() {
//        String cmd = "service firewalld start";
//        List<String> result = LinuxShellTool.sh(cmd);
//        if (StringTool.itemLike(result, "success") > -1) {
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 重启防火墙
//     *
//     * @return
//     */
//    public static boolean restart() {
//        String cmd = "service firewalld restart";
//        List<String> result = LinuxShellTool.sh(cmd);
//        if (StringTool.itemLike(result, "success") > -1) {
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 关闭防火墙
//     *
//     * @return
//     */
//    public static boolean stop() {
//        String cmd = "service firewalld stop";
//        List<String> result = LinuxShellTool.sh(cmd);
//        if (StringTool.itemLike(result, "success") > -1) {
//            return true;
//        }
//        return false;
//    }
//}
