package com.yuzhyn.hidoc.app.application.entity.app;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@TableName("app_conf")
public class AppConf {
    private String item;
    private String key;
    private String value;
    private String ps;
    private Long index;

    public static Map<String, String> toMap(List<AppConf> appConfigList, String item) {
        Map<String, String> rsMap = new HashMap<>();
        if (ListTool.ok(appConfigList)) {
            for (AppConf conf : appConfigList) {
                if (conf.getItem().equals(item)) {
                    rsMap.put(conf.getKey(), conf.getValue());
                }
            }
        }
        return rsMap;
    }
}
