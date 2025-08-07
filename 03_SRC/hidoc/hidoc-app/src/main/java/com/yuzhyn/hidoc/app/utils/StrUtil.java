//package com.yuzhyn.hidoc.app.utils;
//
//import com.yuzhyn.azylee.core.datas.strings.StringTool;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
///**
// * 已经迁移
// */
//public class StrUtil {
//
//    /**
//     * 这里由于公共工具代码异常，暂时在这里修复，等到公共代码发布之后，使用新的公共工具版本库
//     *
//     * @param s
//     * @param regex
//     * @param filterSpace
//     * @param filterRepeat
//     * @return
//     */
//    public static String[] split(String s, String regex, boolean filterSpace, boolean filterRepeat, boolean doTrim) {
//        if (StringTool.ok(s)) {
//            String[] array = s.split(regex);
//            // 过滤空格
//            List<String> list = new ArrayList<String>();
//            Set<String> sets = new HashSet<String>();
//            for (String item : array) {
//                boolean canAdd = false;
//
//                if (filterSpace) {
//                    if (StringTool.ok(item.trim())) canAdd = true;
//                } else {
//                    canAdd = true;
//                }
//
//                if (canAdd) {
//                    if (filterRepeat) {
//                        if (!sets.contains(item)) {
//                            list.add(doTrim ? item.trim() : item);
//                            sets.add(item);
//                        }
//                    } else {
//                        list.add(doTrim ? item.trim() : item);
//                    }
//                }
//            }
//            return list.toArray(new String[list.size()]);
//        } else {
//            return null;
//        }
//    }
//}
