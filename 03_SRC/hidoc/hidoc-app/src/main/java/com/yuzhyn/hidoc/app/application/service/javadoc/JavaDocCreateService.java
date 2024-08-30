package com.yuzhyn.hidoc.app.application.service.javadoc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.type.ReferenceType;
import com.yuzhyn.azylee.core.datas.collections.ArrayTool;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
import com.yuzhyn.azylee.core.datas.strings.StringConst;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.azylee.core.ios.txts.TxtTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.javadoc.*;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.mapper.javadoc.*;
import com.yuzhyn.hidoc.app.application.model.javadoc.JavaDocComment;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class JavaDocCreateService {

    @Autowired
    JavaDocProjectMapper javaDocProjectMapper;

    @Autowired
    JavaDocMetaMapper javaDocMetaMapper;

    @Autowired
    JavaDocMetaLiteMapper javaDocMetaLiteMapper;

    @Autowired
    JavaDocMenuMapper javaDocMenuMapper;

    @Transactional
    public ResponseData uploadZip(String projectName, List<String> fileList, List<String> step) {

        step.add("获取修改用户信息");
        SysUser curUser = CurrentUserManager.getUser();
        if (curUser == null) {
            curUser = CurrentUserManager.getOpenUser();
        }

        boolean isCreateProject = false;
        step.add("查询项目，如果没有则新建项目，如果有则更新版本、时间等信息");
        JavaDocProject javaDocProject = javaDocProjectMapper.selectOne(new LambdaQueryWrapper<JavaDocProject>().eq(JavaDocProject::getName, projectName));
        if (javaDocProject == null) {
            isCreateProject = true;
            javaDocProject = new JavaDocProject();
            javaDocProject.setId("[id:" + projectName + "]");
            javaDocProject.setId(javaDocProject.getId().replace(" ", ""));
            javaDocProject.setName(projectName);
            javaDocProject.setToken(UUIDTool.get());
            javaDocProject.setDescription(projectName);
            javaDocProject.setCreateUserId(curUser.getId());
            javaDocProject.setCreateTime(LocalDateTime.now());
        }
        javaDocProject.setUpdateUserId(curUser.getId());
        javaDocProject.setUpdateTime(LocalDateTime.now());

        step.add("准备解析后的数据列表");
        List<JavaDocMeta> javaDocClassList = new ArrayList<>();
        List<JavaDocMeta> javaDocMethodList = new ArrayList<>();
        List<JavaDocMenu> javaDocMenuList = new ArrayList<>();

        step.add("准备数据开始分解存储");
        step.add("解析项目、类、方法");
        for (String fileItem : fileList) {
            try {
                // parse() 参数可以是 String, File, InputStream等
                parseJavaDoc(fileItem, javaDocProject, javaDocClassList, javaDocMethodList);
            } catch (IOException ex) {
                step.add("解析项目、类、方法异常");
            }
        }

        try {
            parseJavaDocMenu(javaDocProject, javaDocClassList, javaDocMenuList);
            step.add("解析菜单成功");
        } catch (Exception ex) {
            step.add("解析菜单异常");
        }

        step.add("最终汇总数据，如果有异常，则直接退出，不做任何数据落库");
        if (javaDocProject != null && javaDocClassList != null && javaDocMethodList != null) {

            step.add("清理数据库数据");
            cleanJavaDocData(projectName);

            step.add("更新或保存项目信息");
            if (isCreateProject) {
                javaDocProjectMapper.insert(javaDocProject);
            } else {
                javaDocProjectMapper.updateById(javaDocProject);
            }

            step.add("保存类信息");
            if (ListTool.ok(javaDocClassList)) {
                for (JavaDocMeta item : javaDocClassList) {
                    javaDocMetaMapper.insert(item);
                }
            }

            step.add("保存方法信息");
            if (ListTool.ok(javaDocMethodList)) {
                for (JavaDocMeta item : javaDocMethodList) {
                    javaDocMetaMapper.insert(item);
                }
            }

            step.add("保存菜单信息");
            if (ListTool.ok(javaDocMenuList)) {
                for (JavaDocMenu item : javaDocMenuList) {
                    javaDocMenuMapper.insert(item);
                }
            }
            return ResponseData.ok("JavaDoc创建完成");
        }
        return ResponseData.error("没有发现要创建的内容");
    }

    public void cleanJavaDocData(String projectName) {
        JavaDocProject project = javaDocProjectMapper.selectOne(new LambdaQueryWrapper<JavaDocProject>()
                .eq(JavaDocProject::getName, projectName));
        if (project != null) {
            int metaFlag = javaDocMetaLiteMapper.delete(new LambdaQueryWrapper<JavaDocMetaLite>()
                    .eq(JavaDocMetaLite::getProjectId, project.getId()));

            int menuFlag = javaDocMenuMapper.delete(new LambdaQueryWrapper<JavaDocMenu>()
                    .eq(JavaDocMenu::getProjectId, project.getId()));

            log.info("删除历史信息：meta：" + metaFlag + "，menu：" + menuFlag + "。");
        }

    }

    private void parseJavaDoc(String filepath, JavaDocProject javaDocProject, List<JavaDocMeta> javaDocClassList, List<JavaDocMeta> javaDocMethodList) throws FileNotFoundException {
        SysUser curUser = CurrentUserManager.getUser();
        if (curUser == null) {
            curUser = CurrentUserManager.getOpenUser();
        }
        List<String> oriDocTextList = TxtTool.readLine(filepath);
        String oriDocText = String.join(StringConst.NEWLINE, oriDocTextList);

        // parse() 参数可以是 String, File, InputStream等
        CompilationUnit cu = StaticJavaParser.parse(new File(filepath));
        List<TypeDeclaration> typeList = cu.findAll(TypeDeclaration.class);
        // 文件主类名（一个类中，可能存在多个类，如果同包下，有多个重复类，会报主键冲突，导致无法插入数据库）
        // 具体冲突场景如：
        // com.zhangsan 包 Student 类 Bag 子类
        // com.zhangsan 包 Teacher 类 Bag 子类
        // 以上将导致 Bag 子类解析的ID重复，都是同包-同Bag类名的主键
        // 这里增加了主类的名称，作为ID的一部分，避免了上述情况的冲突
        // 极端情况下，不同文件，相同类名还是会用此情况
        // 后续可以考虑从文件名方面入手（需要修改：java解析部分和sh自动化部分）
        String mainClassName = "";

        // 遍历类信息
        if (ListTool.ok(typeList)) {
            for (TypeDeclaration typeItem : typeList) {
                // 创建类信息
                JavaDocMeta javaDocClass = new JavaDocMeta();
                javaDocClass.setMetaType("JavaDocClass");
                javaDocClass.setProjectId(javaDocProject.getId());
                javaDocClass.setProjectName(javaDocProject.getName());
                javaDocClass.setCreateUserId(curUser.getId());
                javaDocClass.setCreateTime(LocalDateTime.now());
                javaDocClass.setName(typeItem.getNameAsString());
                javaDocClass.setIsDeprecated(false);
                if (typeItem.getAnnotations() != null && !typeItem.getAnnotations().isEmpty()) {
                    List<String> annotationsJson = new ArrayList<>();
                    typeItem.getAnnotations().forEach(x -> {
                        String anno = x.toString();
                        annotationsJson.add(anno);
                        if (anno.equals("@Deprecated")) javaDocClass.setIsDeprecated(true);
                    });
                    javaDocClass.setAnnotations(JSON.toJSONString(annotationsJson));
                }
                javaDocClass.setSourceCode(cu.toString());
                javaDocClass.setIsStruct(false);
                if (!StringTool.ok(mainClassName)) mainClassName = typeItem.getNameAsString();

                if (typeItem.hasParentNode() && typeItem.getParentNode().isPresent() && typeItem.getParentNode().get().findCompilationUnit().isPresent()) {
                    CompilationUnit parentNode = typeItem.getParentNode().get().findCompilationUnit().get();
                    // 填充包信息
                    if (parentNode.getPackageDeclaration().isPresent()) {
                        javaDocClass.setPackageInfo(parentNode.getPackageDeclaration().get().getNameAsString());
                        if (javaDocClass.getPackageInfo() == null) javaDocClass.setPackageInfo("");
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
                        javaDocClass.setImports(JSON.toJSONString(importsJson));
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
                    StringBuilder stringBuilder = new StringBuilder();
                    if (typeItem.getComment().isPresent()) {
                        String content = typeItem.getComment().get().getContent();
                        stringBuilder.append(content);
                    }
                    if (ListTool.ok(parentNode.getOrphanComments())) {
                        for (Comment commentItem : parentNode.getOrphanComments()) {
                            stringBuilder.append(commentItem.getContent());
                            stringBuilder.append(StringConst.NEWLINE);
                        }
                    }
                    if (stringBuilder.length() > 0) {

                        JavaDocComment javaDocComment = new JavaDocComment(stringBuilder.toString());
                        javaDocComment.parseComment();
                        javaDocClass.setIsStruct(javaDocComment.isStruct());
                        javaDocClass.setCommentInfo(javaDocComment.getInfo());
                        javaDocClass.setCommentScene(javaDocComment.getScene());
                        javaDocClass.setCommentLimit(javaDocComment.getLimit());
                        javaDocClass.setCommentExample(javaDocComment.getExample());
                        javaDocClass.setCommentLog(JSON.toJSONString(javaDocComment.getLogJson()));
                        javaDocClass.setCommentKeywords(javaDocComment.getKeywords());
                        javaDocClass.setCommentMenu(javaDocComment.getMenu());
                    }
                }
                javaDocClass.setId("[id:" + javaDocProject.getId() + "-" + javaDocClass.getPackageInfo() + "-" + javaDocClass.getQualifier() + "-" + mainClassName + "-" + javaDocClass.getName() + "]");
                javaDocClass.setId(javaDocClass.getId().replace(" ", ""));
                javaDocClassList.add(javaDocClass);

                // 创建方法信息
                if (ListTool.ok(typeItem.getChildNodes())) {
                    for (Node nodeItem : typeItem.getChildNodes()) {
                        if (nodeItem.getClass().equals(MethodDeclaration.class)) {
                            MethodDeclaration methodItem = (MethodDeclaration) nodeItem;
                            JavaDocMeta javaDocMethod = new JavaDocMeta();
                            javaDocMethod.setMetaType("JavaDocMethod");
                            javaDocMethod.setClassId(javaDocClass.getId());
                            javaDocMethod.setClassName(javaDocClass.getName());
                            javaDocMethod.setProjectId(javaDocProject.getId());
                            javaDocMethod.setProjectName(javaDocProject.getName());
                            javaDocMethod.setCreateUserId(curUser.getId());
                            javaDocMethod.setCreateTime(LocalDateTime.now());
                            javaDocMethod.setName(methodItem.getNameAsString());
                            javaDocMethod.setIsDeprecated(false);
                            if (methodItem.getAnnotations() != null && !methodItem.getAnnotations().isEmpty()) {
                                List<String> annotationsJson = new ArrayList<>();
                                methodItem.getAnnotations().forEach(x -> {
                                    String anno = x.toString();
                                    annotationsJson.add(anno);
                                    if (anno.equals("@Deprecated")) javaDocMethod.setIsDeprecated(true);
                                });
                                javaDocMethod.setAnnotations(JSON.toJSONString(annotationsJson));
                            }
                            javaDocMethod.setSourceCode(methodItem.toString());
                            // 填充修饰词
                            if (ListTool.ok(methodItem.getModifiers())) {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (Modifier modItem : methodItem.getModifiers()) {
                                    stringBuilder.append(modItem.getKeyword().asString());
                                    stringBuilder.append(" ");
                                }
                                javaDocMethod.setQualifier(stringBuilder.toString());
                            }
                            // 填充注释内容
                            JavaDocComment javaDocComment = null;
                            if (methodItem.getComment().isPresent()) {
                                String content = methodItem.getComment().get().getContent();
                                javaDocComment = new JavaDocComment(content);
                                javaDocComment.parseComment();
                                javaDocMethod.setIsStruct(javaDocComment.isStruct());
                                javaDocMethod.setReturnDesc(javaDocComment.getReturn());
                                javaDocMethod.setCommentInfo(javaDocComment.getInfo());
                                javaDocMethod.setCommentScene(javaDocComment.getScene());
                                javaDocMethod.setCommentLimit(javaDocComment.getLimit());
                                javaDocMethod.setCommentExample(javaDocComment.getExample());
                                javaDocMethod.setCommentLog(JSON.toJSONString(javaDocComment.getLogJson()));
                                javaDocMethod.setCommentKeywords(javaDocComment.getKeywords());
                                javaDocMethod.setCommentMenu(javaDocComment.getMenu());
                            }
                            // 返回值类型
                            javaDocMethod.setReturnType(methodItem.getTypeAsString());
                            // 填充传入参数
                            StringBuilder paramsTypeString = new StringBuilder();
                            if (ListTool.ok(methodItem.getParameters())) {
                                StringBuilder stringBuilder = new StringBuilder();
                                JSONArray jsonArray = new JSONArray();
                                for (Parameter parameterItem : methodItem.getParameters()) {
                                    paramsTypeString.append("#");
                                    paramsTypeString.append(parameterItem.getTypeAsString());
                                    paramsTypeString.append(":");
                                    paramsTypeString.append(parameterItem.getNameAsString());

                                    stringBuilder.append(parameterItem.getTypeAsString() + " ");
                                    stringBuilder.append(parameterItem.getNameAsString() + " ");
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("type", parameterItem.getTypeAsString());
                                    jsonObject.put("name", parameterItem.getNameAsString());
                                    String desc = "";
                                    if (javaDocComment != null) {
                                        desc = javaDocComment.getParam(parameterItem.getNameAsString());
                                    }
                                    jsonObject.put("desc", desc);
                                    stringBuilder.append(desc + " ");
                                    jsonArray.add(jsonObject);
                                }
                                javaDocMethod.setParams(JSON.toJSONString(jsonArray));
                            }
                            if (ListTool.ok(methodItem.getThrownExceptions())) {
                                StringBuilder stringBuilder = new StringBuilder();
                                JSONArray jsonArray = new JSONArray();
                                for (ReferenceType refItem : methodItem.getThrownExceptions()) {
                                    stringBuilder.append(refItem.asString() + " ");
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("type", refItem.asString());
                                    String desc = "";
                                    if (javaDocComment != null) {
                                        desc = javaDocComment.getThrows(refItem.asString());
                                    }
                                    jsonObject.put("desc", desc);
                                    stringBuilder.append(desc + " ");
                                    jsonArray.add(jsonObject);
                                }
                                javaDocMethod.setThrowses(JSON.toJSONString(jsonArray));
                            }
                            if (javaDocMethod.getParams() == null) javaDocMethod.setParams("");
                            javaDocMethod.setId("[id:" + javaDocClass.getId() + "-" + javaDocMethod.getQualifier() + "-" + javaDocMethod.getReturnType() + "-" + javaDocMethod.getName() + "-" + paramsTypeString + "]");
                            javaDocMethod.setId(javaDocMethod.getId().replace(" ", "-"));
                            javaDocMethodList.add(javaDocMethod);
                        }
                    }
                }
            }
        }
    }

    private void parseJavaDocMenu(JavaDocProject javaDocProject, List<JavaDocMeta> javaDocClassList, List<JavaDocMenu> javaDocMenuList) {
        Map<String, JavaDocMenu> menuMap = new HashMap<>();
        if (ListTool.ok(javaDocClassList)) {
            for (JavaDocMeta classItem : javaDocClassList) {
                if (StringTool.ok(classItem.getCommentMenu()) && StringTool.ok(classItem.getCommentMenu().trim())) {
                    String commentMenu = classItem.getCommentMenu().trim().replace('\\', '/');
                    String[] menuArray = StringTool.split(commentMenu, "/", true, false);
                    String fmtCommentMenu = StringTool.combineArray(menuArray, "/");
                    classItem.setCommentMenu(fmtCommentMenu);

                    for (int i = 0; i < menuArray.length; i++) {
                        String[] innerMenuArray = Arrays.copyOf(menuArray, i + 1);
                        String[] innerParentMenuArray = Arrays.copyOf(menuArray, i);
                        String innerMenuPath = StringTool.combineArray(innerMenuArray, "/");
                        String innerParentMenuPath = StringTool.combineArray(innerParentMenuArray, "/");
                        String parentMenuId = "";
                        if (menuMap.get(innerParentMenuPath) != null) {
                            parentMenuId = menuMap.get(innerParentMenuPath).getId();
                        }

                        JavaDocMenu javaDocMenu = menuMap.get(innerMenuPath);
                        if (javaDocMenu != null) {

                        } else {
                            javaDocMenu = new JavaDocMenu();
                            javaDocMenu.setId("[id:" + javaDocProject.getId() + "-" + innerMenuPath + "]");
                            javaDocMenu.setId(javaDocMenu.getId().replace(" ", ""));
                            javaDocMenu.setMenu(menuArray[i]);
                            javaDocMenu.setMenuPath(innerMenuPath);
                            javaDocMenu.setParentId(parentMenuId);
                            javaDocMenu.setProjectId(javaDocProject.getId());
                            javaDocMenu.setLevel(i + 1);
                            menuMap.put(innerMenuPath, javaDocMenu);
                        }
                    }
                }
            }

            for (JavaDocMenu item : menuMap.values()) {
                javaDocMenuList.add(item);
            }
        }
    }
}
