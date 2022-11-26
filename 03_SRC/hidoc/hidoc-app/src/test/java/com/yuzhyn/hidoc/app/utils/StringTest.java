package com.yuzhyn.hidoc.app.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

public class StringTest {

    /**
     * 测试字符串转大写
     *
     * @param input
     * @param expected
     */
    @ParameterizedTest
    @MethodSource
    public void testUpper(String input, String expected) {

        // 调用测试方法
        String upper = input.toUpperCase();

        // 验证测试结果
        Assertions.assertEquals(expected, upper);
    }

    /**
     * 准备数据
     * https://www.liaoxuefeng.com/wiki/1252599548343744/1304048154181666
     *
     * @return
     */
    static List<Arguments> testUpper() {
        return Arrays.asList(
                Arguments.of("abc", "ABC"),
                Arguments.of("APPLE", "APPLE"),
                Arguments.of("gooD", "GOOD")
        );
    }
}
