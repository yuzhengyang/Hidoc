package com.yuzhyn.hidoc.app.back;

import com.yuzhyn.azylee.core.datas.strings.StringConst;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

public class JavaDocCommentParseBack {
    private String[] parseComment(String s) {
        String[] result = new String[]{"", "", "", "", "", "", ""};

        if (StringTool.ok(s)) {
            String lines[] = s.split("\\r?\\n");
            String curblock = "info";
//            String planBTxt = ""; // 如果没有按照模板书写注释，则按照常规解析内容（普通文本，排除@标志行）
            for (String line : lines) {
                String trimline = line.trim(); // 去掉前后空格的内容
                String txtline = trimline;
                if (txtline.equals("* <p>")) txtline = ""; // 清空掉 "* <p>" 的行
                if (txtline.startsWith("*")) txtline = txtline.substring(1); // 去掉 " *" 的内容
                if (txtline.startsWith(" ")) txtline = txtline.substring(1); // 去掉第一个空格 " " 的内容
                if (txtline.startsWith("@")) continue; // 跳过@修饰的标记内容

                // 分析当前所在区块（>开始，<结束），区块包括：info、scene、limit、example、log、keywords
                Tuple2<String, String> signRs = parseCommentBlockSign(txtline, curblock);
                curblock = signRs.getT1();
                txtline = signRs.getT2();
                int blockid = -1;
                if (curblock.equals("info")) blockid = 0;
                if (curblock.equals("scene")) blockid = 1;
                if (curblock.equals("limit")) blockid = 2;
                if (curblock.equals("example")) blockid = 3;
                if (curblock.equals("log")) blockid = 4;
                if (curblock.equals("keywords")) blockid = 5;
                if (curblock.equals("menu")) blockid = 6;

                // 对planB进行赋值（不在区块中，不是@标志开始的内容）
//                if (curblock.equals("") && (!txtline.trim().startsWith("@") || txtline.trim().toLowerCase().startsWith("@description"))) {
//                    if (txtline.trim().toLowerCase().startsWith("@description")) {
//                        txtline = txtline.trim().substring("@description".length());
//                    }
//                    if (txtline.trim().startsWith(":") || txtline.trim().startsWith("：")) {
//                        txtline = txtline.trim().substring(":".length());
//                    }
//
//                    planBTxt += txtline;
//                    planBTxt += StringConst.NEWLINE;
//                }

                if (blockid >= 0 && blockid < result.length) {
                    result[blockid] += txtline;
                    result[blockid] += StringConst.NEWLINE;
                }
            }

            // 最后整理一下文本内容，缩进一下距离等
            for (int i = 0; i < result.length; i++) {
                result[i] = StringTool.retractSpaceArrayAuto(result[i]);
            }

            // 对planB进行返回（在info内容为空时，返回planB）
//            if (!StringTool.ok(result[0])) result[0] = planBTxt;
        }

        return result;
    }

    private Tuple2<String, String> parseCommentBlockSign(String txtline, String curblock) {
        String result = "";
        if (txtline.startsWith("#场景：") || txtline.startsWith("#场景:")) {
            result = "scene";
            txtline = txtline.substring(4);
        }
        if (txtline.startsWith("#限制：") || txtline.startsWith("#限制:")) {
            result = "limit";
            txtline = txtline.substring(4);
        }
        if (txtline.startsWith("#关键字：") || txtline.startsWith("#关键字:")) {
            result = "keywords";
            txtline = txtline.substring(5);
        }
        if (txtline.startsWith("#目录：") || txtline.startsWith("#目录:")) {
            result = "menu";
            txtline = txtline.substring(4);
        }
        if (txtline.startsWith("<pre>{@code 示例说明")) {
            result = "example>";
        }
        if (txtline.startsWith("<pre>{@code 修改记录")) {
            result = "log>";
        }
        if (txtline.startsWith("}</pre>")) {
            result = "<";
        }

        // 对上次标记进行处理，去除开始和结束标记，可以正式获取文本信息
        if (StringTool.ok(curblock) && curblock.endsWith(">")) {
            curblock = curblock.replaceAll(">", "");
        }

        // 如果当前在某部分中，但是本次未解析到内容，则使用上次的信息
        if (StringTool.ok(curblock) && !StringTool.ok(result)) {
            return Tuples.of(curblock, txtline);
        }
        return Tuples.of(result, txtline);
    }

    private String[] parseCommentOld(String s) {
        String[] result = new String[]{"", "", "", "", "", ""};

        if (StringTool.ok(s)) {
            String lines[] = s.split("\\r?\\n");
            String curblock = "";
            String planBTxt = ""; // 如果没有按照模板书写注释，则按照常规解析内容（普通文本，排除@标志行）
            for (String line : lines) {
                String trimline = line.trim(); // 去掉前后空格的内容
                String txtline = trimline;
                if (txtline.equals("* <p>")) txtline = ""; // 清空掉 "* <p>" 的行
                if (txtline.startsWith("*")) txtline = txtline.substring(1); // 去掉 " *" 的内容

                // 分析当前所在区块（>开始，<结束），区块包括：info、scene、limit、example、log、keywords
                curblock = parseCommentBlockSignOld(txtline, curblock);
                int blockid = -1;
                if (curblock.equals("info")) blockid = 0;
                if (curblock.equals("scene")) blockid = 1;
                if (curblock.equals("limit")) blockid = 2;
                if (curblock.equals("example")) blockid = 3;
                if (curblock.equals("log")) blockid = 4;
                if (curblock.equals("keywords")) blockid = 5;

                // 对planB进行赋值（不在区块中，不是@标志开始的内容）
                if (curblock.equals("") && (!txtline.trim().startsWith("@") || txtline.trim().toLowerCase().startsWith("@description"))) {
                    if (txtline.trim().toLowerCase().startsWith("@description")) {
                        txtline = txtline.trim().substring("@description".length());
                    }
                    if (txtline.trim().startsWith(":") || txtline.trim().startsWith("：")) {
                        txtline = txtline.trim().substring(":".length());
                    }

                    planBTxt += txtline;
                    planBTxt += StringConst.NEWLINE;
                }

                if (blockid >= 0 && blockid < result.length) {
                    result[blockid] += txtline;
                    result[blockid] += StringConst.NEWLINE;
                }
            }

            // 最后整理一下文本内容，缩进一下距离等
            for (int i = 0; i < result.length; i++) {
                result[i] = StringTool.retractSpaceArrayAuto(result[i]);
            }

            // 对planB进行返回（在info内容为空时，返回planB）
            if (!StringTool.ok(result[0])) result[0] = planBTxt;
        }

        return result;
    }

    private String parseCommentBlockSignOld(String txtline, String curblock) {
        String result = "";
        if (txtline.contains("<div")) {
            if (txtline.contains("javadoc=\"info\"") || txtline.contains("javadoc='info'")) {
                result = "info>";
            }
            if (txtline.contains("javadoc=\"scene\"") || txtline.contains("javadoc='scene'")) {
                result = "scene>";
            }
            if (txtline.contains("javadoc=\"limit\"") || txtline.contains("javadoc='limit'")) {
                result = "limit>";
            }
            if (txtline.contains("javadoc=\"example\"") || txtline.contains("javadoc='example'")) {
                result = "example>";
            }
            if (txtline.contains("javadoc=\"log\"") || txtline.contains("javadoc='log'")) {
                result = "log>";
            }
            if (txtline.contains("javadoc=\"keywords\"") || txtline.contains("javadoc='keywords'")) {
                result = "keywords>";
            }
        }
        if (txtline.contains("</div>")) {
            result = "<";
        }

        // 对上次标记进行处理，去除开始和结束标记，可以正式获取文本信息
        if (StringTool.ok(curblock) && curblock.endsWith(">")) {
            curblock = curblock.replaceAll(">", "");
        }

        if (StringTool.ok(result)) {
            // 如果是开始或结束标记，则直接返回
            return result;
        } else {
            // 否则按照传入的标记处理（在遇到结束标记时，返回空白，表示不再任何块中）
            if (curblock.equals("<")) return "";
            else return curblock;
        }
    }
}
