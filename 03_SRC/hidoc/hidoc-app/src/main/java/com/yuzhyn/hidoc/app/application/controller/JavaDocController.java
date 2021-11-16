package com.yuzhyn.hidoc.app.application.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.yuzhyn.hidoc.app.aarg.R;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
                                StringBuilder imports = new StringBuilder();
                                for (ImportDeclaration nodeItem : parentNode.getImports()) {
                                    imports.append(nodeItem.getNameAsString());
                                    importsJson.add(nodeItem.getNameAsString());
                                }
                                javaDocClass.setImports(imports.toString());
                                javaDocClass.setImportsJson(new JSONArray(importsJson));
                            }
                            // 填充修饰词
                            if (ListTool.ok(typeItem.getModifiers())) {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (Object modObject : typeItem.getModifiers()) {
                                    if (modObject.getClass().equals(Modifier.class)) {
                                        Modifier modItem = (Modifier) modObject;
                                        stringBuilder.append(modItem.getKeyword().asString());
                                    }
                                }
                                javaDocClass.setQualifier(stringBuilder.toString());
                            }
                            // 填充注释内容
                            if (ListTool.ok(parentNode.getOrphanComments())) {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (Comment commentItem : parentNode.getOrphanComments()) {
                                    stringBuilder.append(commentItem.getContent());
                                }
                                javaDocClass.setCommentInfo(stringBuilder.toString());
                                javaDocClass.setCommentScene(stringBuilder.toString());
                                javaDocClass.setCommentLimit(stringBuilder.toString());
                                javaDocClass.setCommentExample(stringBuilder.toString());
                                javaDocClass.setCommentLog(stringBuilder.toString());
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
                                        javaDocMethod.setCommentInfo(content);
                                        javaDocMethod.setCommentScene(content);
                                        javaDocMethod.setCommentLimit(content);
                                        javaDocMethod.setCommentExample(content);
                                        javaDocMethod.setCommentLog(content);
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

    private String getComment(int type, String s) {
        /*
        * javaDocClass.setCommentInfo(stringBuilder.toString());
          javaDocClass.setCommentScene(stringBuilder.toString());
          javaDocClass.setCommentLimit(stringBuilder.toString());
          javaDocClass.setCommentExample(stringBuilder.toString());
          javaDocClass.setCommentLog(stringBuilder.toString());
        * */
        StringBuilder stringBuilder = new StringBuilder();
        String headFlag = "";
        switch (type) {
            case 1:
                headFlag = "";
                break;
            case 2:
                headFlag = "";
                break;
            case 3:
                headFlag = "";
                break;
            case 4:
                headFlag = "";
                break;
            case 5:
                headFlag = "";
                break;
        }
        return "";
    }
}
