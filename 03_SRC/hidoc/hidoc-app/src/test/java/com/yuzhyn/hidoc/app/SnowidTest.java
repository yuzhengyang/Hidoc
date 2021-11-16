package com.yuzhyn.hidoc.app;

import com.yuzhyn.azylee.core.datas.ids.SnowFlake;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SnowidTest {

    public static void main(String[] args) {
        System.out.println("SnowFlake id 测试 开始");
        Set<String> sets = new HashSet<>();
        SnowFlake snowFlake = new SnowFlake(1, 1);
        for (int i = 0; i < 500000; i++) {
            String id = snowFlake.nexts();
            if (sets.contains(id)) {
                System.out.println("id 重复 " + id);
            }
            sets.add(id);
        }
        System.out.println("SnowFlake id 测试 结束");
    }
}
