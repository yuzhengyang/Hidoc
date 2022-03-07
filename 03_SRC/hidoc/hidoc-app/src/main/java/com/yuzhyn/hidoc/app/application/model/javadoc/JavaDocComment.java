package com.yuzhyn.hidoc.app.application.model.javadoc;

import com.github.javaparser.ast.comments.JavadocComment;
import com.yuzhyn.azylee.core.datas.strings.StringConst;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import reactor.util.function.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class JavaDocComment {

    private String comment;

    private List<JavaDocCommentStruct> structList;

    private JavaDocComment() {
    }

    public JavaDocComment(String _comment) {
        this.comment = _comment;
        this.structList = new ArrayList<>();
    }

    public String[] parseComment() {
        parseCommentType();


        for (JavaDocCommentStruct item : structList) {
            System.out.println(item.type + " -> " + item.txt);
        }
        return null;
    }


    /**
     * 按行解析注释类型
     */
    private void parseCommentType() {
        if (StringTool.ok(comment)) {
            String lines[] = comment.split("\\r?\\n");
            String curblock = "info";
            for (String line : lines) {
                String trimline = line.trim(); // 去掉前后空格的内容
                String txtline = trimline;
                if (txtline.equals("* <p>")) txtline = ""; // 清空掉 "* <p>" 的行
                if (txtline.startsWith("*")) txtline = txtline.substring(1); // 去掉 " *" 的内容
                if (txtline.startsWith(" ")) txtline = txtline.substring(1); // 去掉第一个空格 " " 的内容
//                if (txtline.startsWith("@")) continue; // 跳过@修饰的标记内容


                if (txtline.startsWith("#场景：") || txtline.startsWith("#场景:")) {
                    curblock = "scene";
                    txtline = txtline.substring(4);
                } else if (txtline.startsWith("#限制：") || txtline.startsWith("#限制:")) {
                    curblock = "limit";
                    txtline = txtline.substring(4);
                } else if (txtline.startsWith("#关键字：") || txtline.startsWith("#关键字:")) {
                    curblock = "keywords";
                    txtline = txtline.substring(5);
                } else if (txtline.startsWith("#目录：") || txtline.startsWith("#目录:")) {
                    curblock = "menu";
                    txtline = txtline.substring(4);
                } else if (txtline.startsWith("<pre>{@code 示例说明")) {
                    curblock = "example>";
                } else if (txtline.startsWith("<pre>{@code 修改记录")) {
                    curblock = "log>";
                } else if (txtline.startsWith("}</pre>")) {
                    curblock = "";
                } else if (txtline.startsWith("@param")) {
                    curblock = "@param";
                    txtline = txtline.substring(6);
                } else if (txtline.startsWith("@return")) {
                    curblock = "@return";
                    txtline = txtline.substring(7);
                } else {
                    // 对其他内容进行分析
                    // 对进入块后进行处理
                    if (curblock.equals("example>")) {
                        curblock = "example";
                    } else if (curblock.equals("log>")) {
                        curblock = "log";
                    }
                }
                structList.add(new JavaDocCommentStruct(curblock, txtline));

            }
//
//            // 最后整理一下文本内容，缩进一下距离等
//            for (int i = 0; i < result.length; i++) {
//                result[i] = StringTool.retractSpaceArrayAuto(result[i]);
//            }

            // 对planB进行返回（在info内容为空时，返回planB）
//            if (!StringTool.ok(result[0])) result[0] = planBTxt;
        }
    }
}
