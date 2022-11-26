package com.yuzhyn.hidoc.app.utils;

import com.yuzhyn.azylee.core.datas.ids.SnowFlake;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.Set;

public class SnowFlakeTest {

    @ParameterizedTest
    @MethodSource
    public void testGen() {
        SnowFlake snowFlake = new SnowFlake(0, 0, 0);
        for (int i = 0; i < 10000; i++) {
            System.out.println(snowFlake.nexts());
        }
    }


//    public static void main(String[] args) {
//        System.out.println("SnowFlake id 测试 开始");
//        Set<String> sets = new HashSet<>();
//        SnowFlake snowFlake = new SnowFlake(1, 1);
//        for (int i = 0; i < 500000; i++) {
//            String id = snowFlake.nexts();
//            if (sets.contains(id)) {
//                System.out.println("id 重复 " + id);
//            }
//            sets.add(id);
//        }
//        System.out.println("SnowFlake id 测试 结束");
//    }
}
