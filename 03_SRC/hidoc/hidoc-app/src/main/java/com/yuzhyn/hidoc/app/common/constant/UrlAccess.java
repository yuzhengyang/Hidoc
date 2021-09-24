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
        urls.add("/user/register");
        urls.add("/user/login");
//        urls.add("/user/logout");
        urls.add("/collected/preview");
        urls.add("/collected/test");
        urls.add("/collected/test2");
        urls.add("/collected/get");
        urls.add("/doc/get");
        urls.add("/f/d");
        return urls;
    }
}
