package com.yuzhyn.hidoc.app.common.constant;

import com.yuzhyn.hidoc.app.manager.CurrentUserManager;

import java.util.ArrayList;
import java.util.List;

public class UrlAccess {

    public static boolean isAnonymous(String url) {
        boolean isAccess = false;
        // 根据api列表，放开指定接口
        for (String item : anonymous()) {
            if (item.endsWith("*")) {
                if (url.startsWith(item.replace("*", ""))) {
                    isAccess = true;
                    break;
                }
            } else {
                if (url.equals(item)) {
                    isAccess = true;
                    break;
                }
            }
        }
        // 根据api前缀，控制开放接口
        if(url.startsWith("/openapi/")){
            isAccess = true;
        }

        if(isAccess){
            CurrentUserManager.createOpenUser();
        }else{
            CurrentUserManager.cleanOpenUser();
        }
        return isAccess;
    }

    public static List<String> anonymous() {
        List<String> urls = new ArrayList<>();
        // 用户注册
        urls.add("/user/register");
        // 获取验证码
        urls.add("/user/getAuthCode");
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
        urls.add("/f/d/*");
        // 数据收集器接入
        urls.add("/datacoll/create");
//        // javadocapi搜索
//        urls.add("/javadocapi/search");
//        urls.add("/javadocapi/projectList");
//        urls.add("/javadocapi/packageList");
//        urls.add("/javadocapi/classList");
//        urls.add("/javadocapi/classDetail");
//        urls.add("/javadocapi/methodList");
//        urls.add("/javadocapi/menuList");
//        // javadocapi查询源文件
//        urls.add("/javadocapi/getOriginalDocument");
//        // javadocapi查询方法源码
//        urls.add("/javadocapi/getSourceCode");
//        // javadocapi上传文件api
//        urls.add("/javadocapi/uploadZip*");
        // 其他测试入口
//        urls.add("/collected/test");
//        urls.add("/collected/test2");
        return urls;
    }
}
