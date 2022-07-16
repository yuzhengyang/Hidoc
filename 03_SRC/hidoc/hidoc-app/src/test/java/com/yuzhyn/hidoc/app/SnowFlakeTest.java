package com.yuzhyn.hidoc.app;

import com.yuzhyn.azylee.core.datas.ids.SnowFlake;

public class SnowFlakeTest {
    public static void main(String[] args) {
        SnowFlake snowFlake = new SnowFlake(0, 0, 0);
        for (int i = 0; i < 10000; i++) {
            System.out.println(snowFlake.nexts());
        }
    }
}
