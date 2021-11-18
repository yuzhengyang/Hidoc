package com.yuzhyn.hidoc.app.application.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringConst;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.doc.Doc;
import com.yuzhyn.hidoc.app.application.entity.doc.DocHistoryLite;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocClass;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocMethod;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocProject;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.mapper.javadoc.JavaDocClassMapper;
import com.yuzhyn.hidoc.app.application.mapper.javadoc.JavaDocMethodMapper;
import com.yuzhyn.hidoc.app.application.mapper.javadoc.JavaDocProjectMapper;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.omg.CORBA.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("javadoc")
public class JavaDocController {

    @Autowired
    JavaDocProjectMapper javaDocProjectMapper;

    @Autowired
    JavaDocClassMapper javaDocClassMapper;

    @Autowired
    JavaDocMethodMapper javaDocMethodMapper;

    @PostMapping("search")
    public ResponseData search(@RequestBody Map<String, Object> params) {
        String mode = MapTool.get(params, "mode", "").toString();
        String text = MapTool.get(params, "text", "").toString();
        String textLike = "%" + text.replace(' ', '%') + "%";

        ResponseData responseData = ResponseData.ok();
        IPage<JavaDocClass> classPage = javaDocClassMapper.selectPage(new Page<JavaDocClass>(1, 100), new LambdaQueryWrapper<JavaDocClass>()
                .or().like(JavaDocClass::getName, textLike)
                .or().like(JavaDocClass::getCommentInfo, textLike)
                .or().like(JavaDocClass::getCommentScene, textLike)
                .or().like(JavaDocClass::getCommentLimit, textLike)
                .or().like(JavaDocClass::getCommentExample, textLike)
                .or().like(JavaDocClass::getCommentLog, textLike)
                .or().like(JavaDocClass::getCommentKeywords, textLike));
        List<JavaDocClass> classList = classPage.getRecords();

        IPage<JavaDocMethod> methodPage = javaDocMethodMapper.selectPage(new Page<JavaDocMethod>(1, 100), new LambdaQueryWrapper<JavaDocMethod>()
                .or().like(JavaDocMethod::getName, textLike)
                .or().like(JavaDocMethod::getCommentInfo, textLike)
                .or().like(JavaDocMethod::getCommentScene, textLike)
                .or().like(JavaDocMethod::getCommentLimit, textLike)
                .or().like(JavaDocMethod::getCommentExample, textLike)
                .or().like(JavaDocMethod::getCommentLog, textLike)
                .or().like(JavaDocMethod::getCommentKeywords, textLike));
        List<JavaDocMethod> methodList = methodPage.getRecords();

        List<String> methodClassIdList = methodList.stream().map(JavaDocMethod::getClassId).distinct().collect(toList());
        List<JavaDocClass> methodClassList = javaDocClassMapper.selectBatchIds(methodClassIdList);

        for (JavaDocMethod methodItem : methodList) {
            for (JavaDocClass classItem : methodClassList) {
                if (methodItem.getClassId().equals(classItem.getId())) {
                    methodItem.setJavaDocClass(classItem);
                }
            }
        }

        List<Object> objectList = new ArrayList<>();
        objectList.addAll(classList);
        objectList.addAll(methodList);
        responseData.putData(objectList);
        return responseData;
    }

    @PostMapping({"upload", "u"})
    @Transactional
    public ResponseData upload(@RequestParam(value = "projectId") String projectId, @RequestParam("file") MultipartFile[] files) {

        // 参数判断检查
        if (!ListTool.ok(files)) return ResponseData.error("请选择文件");

        JavaDocProject javaDocProject = javaDocProjectMapper.selectById(projectId);
        if (javaDocProject == null) return ResponseData.error("JavaDoc项目不存在");

        SysUser curUser = CurrentUserManager.getUser();

        // 更新项目信息
        javaDocProject.setUpdateUserId(curUser.getId());
        javaDocProject.setUpdateTime(LocalDateTime.now());
        javaDocProject.setVersion(String.valueOf(System.currentTimeMillis()));
        List<JavaDocClass> javaDocClassList = new ArrayList<>();
        List<JavaDocMethod> javaDocMethodList = new ArrayList<>();

        // 准备数据开始分解存储
        for (MultipartFile file : files) {
            try {
                // parse() 参数可以是 String, File, InputStream等
                CompilationUnit cu = StaticJavaParser.parse(file.getInputStream());
                List<TypeDeclaration> typeList = cu.findAll(TypeDeclaration.class);

                // 遍历类信息
                if (ListTool.ok(typeList)) {
                    for (TypeDeclaration typeItem : typeList) {
                        // 创建类信息
                        JavaDocClass javaDocClass = new JavaDocClass();
                        javaDocClass.setId(R.SnowFlake.nexts());
                        javaDocClass.setProjectId(javaDocProject.getId());
                        javaDocClass.setVersion(javaDocProject.getVersion());
                        javaDocClass.setCreateUserId(curUser.getId());
                        javaDocClass.setCreateTime(LocalDateTime.now());
                        javaDocClass.setName(typeItem.getNameAsString());

                        if (typeItem.hasParentNode() && typeItem.getParentNode().isPresent() && typeItem.getParentNode().get().findCompilationUnit().isPresent()) {
                            CompilationUnit parentNode = typeItem.getParentNode().get().findCompilationUnit().get();
                            // 填充包信息
                            if (parentNode.getPackageDeclaration().isPresent()) {
                                javaDocClass.setPackageInfo(parentNode.getPackageDeclaration().get().getNameAsString());
                            }
                            // 填充引用信息
                            if (ListTool.ok(parentNode.getImports())) {
                                List<Object> importsJson = new ArrayList<>(parentNode.getImports().size());
                                StringBuilder stringBuilder = new StringBuilder();
                                for (ImportDeclaration nodeItem : parentNode.getImports()) {
                                    stringBuilder.append(nodeItem.getNameAsString());
                                    stringBuilder.append(" ");
                                    importsJson.add(nodeItem.getNameAsString());
                                }
                                javaDocClass.setImports(stringBuilder.toString());
                                javaDocClass.setImportsJson(new JSONArray(importsJson));
                            }
                            // 填充修饰词
                            if (ListTool.ok(typeItem.getModifiers())) {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (Object modObject : typeItem.getModifiers()) {
                                    if (modObject.getClass().equals(Modifier.class)) {
                                        Modifier modItem = (Modifier) modObject;
                                        stringBuilder.append(modItem.getKeyword().asString());
                                        stringBuilder.append(" ");
                                    }
                                }
                                javaDocClass.setQualifier(stringBuilder.toString());
                            }
                            // 填充注释内容
                            if (ListTool.ok(parentNode.getOrphanComments())) {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (Comment commentItem : parentNode.getOrphanComments()) {
                                    stringBuilder.append(commentItem.getContent());
                                    stringBuilder.append(StringConst.NEWLINE);
                                }
                                String[] commentArrays = parseComment(stringBuilder.toString());
                                javaDocClass.setCommentInfo(commentArrays[0]);
                                javaDocClass.setCommentScene(commentArrays[1]);
                                javaDocClass.setCommentLimit(commentArrays[2]);
                                javaDocClass.setCommentExample(commentArrays[3]);
                                javaDocClass.setCommentLog(commentArrays[4]);
                                javaDocClass.setCommentKeywords(commentArrays[5]);

                                javaDocClass.setCommentLogJson(parseCommentLog(commentArrays[4]));
                            }
                        }
                        javaDocClassList.add(javaDocClass);

                        // 创建方法信息
                        if (ListTool.ok(typeItem.getChildNodes())) {
                            for (Node nodeItem : typeItem.getChildNodes()) {
                                if (nodeItem.getClass().equals(MethodDeclaration.class)) {
                                    MethodDeclaration methodItem = (MethodDeclaration) nodeItem;
                                    JavaDocMethod javaDocMethod = new JavaDocMethod();
                                    javaDocMethod.setId(R.SnowFlake.nexts());
                                    javaDocMethod.setClassId(javaDocClass.getId());
                                    javaDocMethod.setProjectId(javaDocProject.getId());
                                    javaDocMethod.setVersion(javaDocProject.getVersion());
                                    javaDocMethod.setCreateUserId(curUser.getId());
                                    javaDocMethod.setCreateTime(LocalDateTime.now());
                                    javaDocMethod.setName(methodItem.getNameAsString());
                                    // 填充修饰词
                                    if (ListTool.ok(methodItem.getModifiers())) {
                                        StringBuilder stringBuilder = new StringBuilder();
                                        for (Modifier modItem : methodItem.getModifiers()) {
                                            stringBuilder.append(modItem.getKeyword().asString());
                                        }
                                        javaDocMethod.setQualifier(stringBuilder.toString());
                                    }
                                    // 填充传入参数
                                    javaDocMethod.setReturnType(methodItem.getTypeAsString());
                                    if (ListTool.ok(methodItem.getParameters())) {
                                        StringBuilder stringBuilder = new StringBuilder();
                                        JSONArray jsonArray = new JSONArray();
                                        for (Parameter parameterItem : methodItem.getParameters()) {
                                            stringBuilder.append(parameterItem.getTypeAsString());
                                            stringBuilder.append(parameterItem.getNameAsString());
                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("type", parameterItem.getTypeAsString());
                                            jsonObject.put("name", parameterItem.getNameAsString());
                                            jsonArray.add(jsonObject);
                                        }
                                        javaDocMethod.setParams(stringBuilder.toString());
                                        javaDocMethod.setParamsJson(jsonArray);
                                    }
                                    // 填充注释内容
                                    if (methodItem.getComment().isPresent()) {
                                        String content = methodItem.getComment().get().getContent();
                                        String[] commentArrays = parseComment(content);
                                        javaDocMethod.setCommentInfo(commentArrays[0]);
                                        javaDocMethod.setCommentScene(commentArrays[1]);
                                        javaDocMethod.setCommentLimit(commentArrays[2]);
                                        javaDocMethod.setCommentExample(commentArrays[3]);
                                        javaDocMethod.setCommentLog(commentArrays[4]);
                                        javaDocMethod.setCommentKeywords(commentArrays[5]);

                                        javaDocMethod.setCommentLogJson(parseCommentLog(commentArrays[4]));
                                    }
                                    javaDocMethodList.add(javaDocMethod);
                                }
                            }
                        }
                    }
                }
            } catch (IOException ex) {
            }
        }

        // 最终汇总数据
        if (javaDocProject != null && javaDocClassList != null && javaDocMethodList != null) {
            javaDocProjectMapper.updateById(javaDocProject);

            if (ListTool.ok(javaDocClassList)) {
                for (JavaDocClass item : javaDocClassList) {
                    javaDocClassMapper.insert(item);
                }
            }

            if (ListTool.ok(javaDocMethodList)) {
                for (JavaDocMethod item : javaDocMethodList) {
                    javaDocMethodMapper.insert(item);
                }
            }
        }
        return ResponseData.ok("JavaDoc创建完成");
    }

    private String[] parseComment(String s) {
        String[] result = new String[]{"", "", "", "", "", ""};

        if (StringTool.ok(s)) {
            String lines[] = s.split("\\r?\\n");
            String curblock = "";
//            StringBuilder html = new StringBuilder();
            for (String line : lines) {
                String trimline = line.trim(); // 去掉前后空格的内容
                String txtline = trimline;
                if (txtline.equals("* <p>")) txtline = ""; // 清空掉 "* <p>" 的行
                if (txtline.startsWith("*")) txtline = txtline.substring(1); // 去掉 " *" 的内容

//                html.append(txtline);
//                html.append(StringConst.NEWLINE);

                // 分析当前所在区块（>开始，<结束），区块包括：info、scene、limit、example、log、keywords
                curblock = parseCommentBlockSign(txtline, curblock);
                int blockid = -1;
                if (curblock.equals("info")) blockid = 0;
                if (curblock.equals("scene")) blockid = 1;
                if (curblock.equals("limit")) blockid = 2;
                if (curblock.equals("example")) blockid = 3;
                if (curblock.equals("log")) blockid = 4;
                if (curblock.equals("keywords")) blockid = 5;

                if (blockid >= 0 && blockid < result.length) {
                    result[blockid] += txtline;
                    result[blockid] += StringConst.NEWLINE;
                }
            }

            // 最后整理一下文本内容，缩进一下距离等
            for (int i = 0; i < result.length; i++) {
                result[i] = StringTool.retractSpaceArrayAuto(result[i]);
            }

//            Document doc = Jsoup.parse(html.toString());
//            System.out.println(doc); // 输出带标签的html文档
        }

        return result;
    }

    private String parseCommentBlockSign(String txtline, String curblock) {
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

    private JSONArray parseCommentLog(String s) {
        JSONArray jsonArray = new JSONArray();
        String[] lines = StringTool.splitLine(s);
        for (String line : lines) {
            if (line.contains("版本") && line.contains("修改时间") && line.contains("修改人") && line.contains("修改内容")) {
                // 这行肯定是标题啦
            } else {
                String[] logs = StringTool.splitLine(line, "  ", 4, " ", true);
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
}
