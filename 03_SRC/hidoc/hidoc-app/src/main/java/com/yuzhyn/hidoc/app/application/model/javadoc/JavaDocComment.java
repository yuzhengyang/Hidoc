package com.yuzhyn.hidoc.app.application.model.javadoc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.javaparser.ast.comments.JavadocComment;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.objects.ObjectTool;
import com.yuzhyn.azylee.core.datas.strings.StringConst;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import reactor.util.function.Tuple2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaDocComment {

    private Boolean isStruct;

    private String comment;

    private List<JavaDocCommentStruct> structList;

    private Map<String, String> commentResult;

    private JavaDocComment() {
    }

    public JavaDocComment(String _comment) {
        this.isStruct = false;
        this.comment = _comment;
        this.structList = new ArrayList<>();
        this.commentResult = new HashMap<>();
    }

    public void parseComment() {
        parseCommentType();
        formatComment();
        if (commentResult.containsKey("info") || commentResult.containsKey("scene") || commentResult.containsKey("limit") ||
                commentResult.containsKey("keywords") || commentResult.containsKey("menu") || commentResult.containsKey("example")) {
            this.isStruct = true;
        }
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

                if (txtline.startsWith("#场景：") || txtline.startsWith("#场景:") || txtline.startsWith("#场景 ")) {
                    curblock = "scene";
                    txtline = txtline.substring(4);
                } else if (txtline.startsWith("#限制：") || txtline.startsWith("#限制:") || txtline.startsWith("#限制 ")) {
                    curblock = "limit";
                    txtline = txtline.substring(4);
                } else if (txtline.startsWith("#关键字：") || txtline.startsWith("#关键字:") || txtline.startsWith("#关键字 ")) {
                    curblock = "keywords";
                    txtline = txtline.substring(5);
                } else if (txtline.startsWith("#目录：") || txtline.startsWith("#目录:") || txtline.startsWith("#目录 ")) {
                    curblock = "menu";
                    txtline = txtline.substring(4);
                } else if (txtline.startsWith("<pre>{@code 示例说明")) {
                    curblock = "example>";
                } else if (txtline.startsWith("<pre>{@code 修改记录")) {
                    curblock = "log>";
                } else if (StringTool.has(txtline, "版本", "修改时间", "修改人", "修改内容")) {
                    curblock = "log>";
                } else if (txtline.startsWith("}</pre>")) {
                    if (curblock.startsWith("example")) {
                        curblock = "example<";
                    } else {
                        curblock = "";
                    }
                } else if (txtline.startsWith("@param")) {
                    curblock = "@param";
                    txtline = txtline.substring(6);

                    String[] params = StringTool.splitLine(txtline.trim(), " ", 2, "", true);
                    if (StringTool.ok(params[0], params[1])) {
                        curblock += " " + params[0];
                        txtline = params[1];
                    }

                } else if (txtline.startsWith("@throws")) {
                    curblock = "@throws";
                    txtline = txtline.substring(7);

                    String[] params = StringTool.splitLine(txtline.trim(), " ", 2, "", true);
                    if (StringTool.ok(params[0], params[1])) {
                        curblock += " " + params[0];
                        txtline = params[1];
                    }

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
        }
    }

    private void formatComment() {
        if (ListTool.ok(structList)) {
            for (JavaDocCommentStruct structItem : structList) {
                String s = ObjectTool.optional(commentResult.get(structItem.getType()), "");
                String txt = structItem.getTxt();
                // 合并预处理
                if (structItem.getType().startsWith("@param")) {
                    txt = txt.trim();
                }

                if (StringTool.ok(s)) {
                    s += StringConst.NEWLINE;
                }
                s += txt;
                commentResult.put(structItem.getType(), s);
            }
        }

//        System.out.println("原始内容");
//        for (JavaDocCommentStruct item : structList) {
//            System.out.println(item.getType() + " : " + item.getTxt());
//        }
//
//        System.out.println("解析内容");
//        for (String item : commentResult.keySet()) {
//            System.out.println(item + " : " + commentResult.get(item));
//        }

        for (String item : commentResult.keySet()) {
            commentResult.put(item, StringTool.retractSpaceArrayAuto(commentResult.get(item)));
        }

//        System.out.println("最后整理内容");
//        for (String item : commentResult.keySet()) {
//            System.out.println(item + " : " + commentResult.get(item));
//        }
    }

    public String getInfo() {
        return ObjectTool.optional(commentResult.get("info"), "");
    }

    public String getScene() {
        return ObjectTool.optional(commentResult.get("scene"), "");
    }

    public String getLimit() {
        return ObjectTool.optional(commentResult.get("limit"), "");
    }

    public String getKeywords() {
        return ObjectTool.optional(commentResult.get("keywords"), "");
    }

    public String getMenu() {
        return ObjectTool.optional(commentResult.get("menu"), "");
    }

    public String getExample() {
        return ObjectTool.optional(commentResult.get("example"), "");
    }

    public String getLog() {
        return ObjectTool.optional(commentResult.get("log"), "");
    }

    public JSONArray getLogJson() {
        String s = getLog();
        JSONArray jsonArray = new JSONArray();
        String[] lines = StringTool.splitLine(s);
        for (String line : lines) {
            String[] logs = StringTool.splitLine(line, "  ", 4, " ", true);
            if (StringTool.ok(logs[0], logs[1], logs[2], logs[3])) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("version", logs[0]);
                jsonObject.put("time", logs[1]);
                jsonObject.put("author", logs[2]);
                jsonObject.put("content", logs[3]);
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    public String getParam(String pname) {
        return ObjectTool.optional(commentResult.get("@param " + pname), "");
    }

    public String getThrows(String tname) {
        return ObjectTool.optional(commentResult.get("@throws " + tname), "");
    }

    public String getReturn() {
        return ObjectTool.optional(commentResult.get("@return"), "");
    }

    public boolean isStruct() {
        return isStruct;
    }

}
