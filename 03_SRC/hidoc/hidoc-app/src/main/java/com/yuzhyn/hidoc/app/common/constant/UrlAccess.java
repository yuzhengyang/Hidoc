package com.yuzhyn.hidoc.app.common.constant;

import java.util.ArrayList;
import java.util.List;

public class UrlAccess {

    public static boolean isAnonymous(String url) {
        for (String item : anonymous()) {
            if (url.startsWith(item)) return true;
        }
        return false;
    }

    public static List<String> anonymous() {
        List<String> urls = new ArrayList<>();
        // 用户注册
        urls.add("/user/register");
        // 用户登录
        urls.add("/user/login");
        // 用户登出
        urls.add("/user/logout");
        // 文集（公开文集列表）
        urls.add("/collected/preview");
        // 文集详情
        urls.add("/collected/get");
        // 文档详情
        urls.add("/doc/get");
        // 文件下载
        urls.add("/f/d");
        // 数据收集器接入
        urls.add("/datacoll/create");
        // 其他测试入口
//        urls.add("/collected/test");
//        urls.add("/collected/test2");
        return urls;
    }
}
