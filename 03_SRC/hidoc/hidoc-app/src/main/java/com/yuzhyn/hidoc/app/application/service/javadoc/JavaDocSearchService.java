package com.yuzhyn.hidoc.app.application.service.javadoc;

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
import com.yuzhyn.azylee.core.datas.strings.HtmlStringTool;
import com.yuzhyn.azylee.core.datas.strings.StringConst;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.azylee.core.ios.txts.TxtTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.javadoc.*;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.mapper.javadoc.*;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class JavaDocSearchService {

    @Autowired
    JavaDocProjectMapper javaDocProjectMapper;

    @Autowired
    JavaDocClassMapper javaDocClassMapper;

    @Autowired
    JavaDocClassLiteMapper javaDocClassLiteMapper;

    @Autowired
    JavaDocMethodMapper javaDocMethodMapper;

    @Autowired
    JavaDocMethodLiteMapper javaDocMethodLiteMapper;

    public ResponseData search(Map<String, Object> params) {

        String mode = MapTool.get(params, "mode", "").toString();
        String name = MapTool.get(params, "name", "").toString();
        String text = MapTool.get(params, "text", "").toString();
        String[] nameArray = StringTool.split(name, " ", true, true);
        String[] textArray = StringTool.split(text, " ", true, true);
//        String textLike = "%" + text.replace(' ', '%') + "%"; // 替换空格为通配符有局限性，比如有强制的前后顺序
        String begTag = "<span style='color:red;'>";
        String endTag = "</span>";

        // 不输入任何信息，直接搜索是没有意义的，这里限制搜索个数，仅做示意
        int selectClassPageSize = 5, selectMethodPageSize = 5;
        if (StringTool.ok(name) || StringTool.ok(text)) {
            selectClassPageSize = 50;
            selectMethodPageSize = 50;
            if (mode.equals("class")) selectClassPageSize = 100;
            if (mode.equals("method")) selectMethodPageSize = 100;
        }

        ResponseData responseData = ResponseData.ok();
        List<JavaDocProject> projectList = javaDocProjectMapper.selectList(null);

        if (ListTool.ok(projectList)) {
            List<String> projectVersionList = projectList.stream().map(JavaDocProject::getVersion).distinct().collect(toList());
            List<JavaDocClassLite> classList = null;
            List<JavaDocMethodLite> methodList = null;

            if (mode.equals("class") || mode.equals("all")) {

                LambdaQueryWrapper<JavaDocClassLite> classWrapper = new LambdaQueryWrapper<JavaDocClassLite>();
                classWrapper = classWrapper.and(p -> p.in(JavaDocClassLite::getVersion, projectVersionList)).and(q -> q.eq(JavaDocClassLite::getIsStruct, true));
                if (ListTool.ok(nameArray)) {
                    classWrapper = classWrapper.and(p -> {
                        for (String key : nameArray) {
                            String keyLike = "%" + key + "%";
                            p.apply("COALESCE(name,'') ILIKE {0}", keyLike);
//                                    "COALESCE(project_name,'')||" +
//                                    "COALESCE(comment_info,'')||" +
//                                    "COALESCE(comment_scene,'')||" +
//                                    "COALESCE(comment_limit,'')||" +
//                                    "COALESCE(comment_keywords,'')
//                            .or().apply("comment_example ILIKE {0}", keyLike) // 预览页面上不直接展示的内容，不提供搜索
//                            .or().apply("comment_log ILIKE {0}", keyLike) // 预览页面上不直接展示的内容，不提供搜索
                        }
                    });
                }
                if (ListTool.ok(textArray)) {
                    classWrapper = classWrapper.and(p -> {
                        for (String key : textArray) {
                            String keyLike = "%" + key + "%";
                            p.apply(
//                                    "COALESCE(name,'')||" +
//                                    "COALESCE(project_name,'')||" +
                                    "COALESCE(comment_info,'')||" +
                                            "COALESCE(comment_scene,'')||" +
                                            "COALESCE(comment_limit,'')||" +
                                            "COALESCE(comment_keywords,'') ILIKE {0}", keyLike);
//                            .or().apply("comment_example ILIKE {0}", keyLike) // 预览页面上不直接展示的内容，不提供搜索
//                            .or().apply("comment_log ILIKE {0}", keyLike) // 预览页面上不直接展示的内容，不提供搜索
                        }
                    });
                }

                IPage<JavaDocClassLite> classPage = javaDocClassLiteMapper.selectPage(new Page<JavaDocClassLite>(1, selectClassPageSize), classWrapper);
                classList = classPage.getRecords();
                // 后端关键字高亮
                // bug：导致内容被抹除
//                if (ListTool.ok(classList)) {
//                    for (JavaDocClassLite classItem : classList) {
//                        classItem.setCommentInfo(HtmlStringTool.keywordsHightLight(HtmlStringTool.newLineBrFotmat(classItem.getCommentInfo()), text, begTag, endTag));
//                        classItem.setCommentScene(HtmlStringTool.keywordsHightLight(HtmlStringTool.newLineBrFotmat(classItem.getCommentScene()), text, begTag, endTag));
//                        classItem.setCommentLimit(HtmlStringTool.keywordsHightLight(HtmlStringTool.newLineBrFotmat(classItem.getCommentLimit()), text, begTag, endTag));
////                        classItem.setCommentExample(HtmlStringTool.keywordsHightLight(classItem.getCommentExample(), text, begTag, endTag));
////                        classItem.setCommentLog(HtmlStringTool.keywordsHightLight(classItem.getCommentLog(), text, begTag, endTag));
//                        classItem.setCommentKeywords(HtmlStringTool.keywordsHightLight(classItem.getCommentKeywords(), text, begTag, endTag));
//                    }
//                }
            }
            if (mode.equals("method") || mode.equals("all")) {

                LambdaQueryWrapper<JavaDocMethodLite> methodWrapper = new LambdaQueryWrapper<JavaDocMethodLite>();
                methodWrapper = methodWrapper.and(p -> p.in(JavaDocMethodLite::getVersion, projectVersionList)).and(q -> q.eq(JavaDocMethodLite::getIsStruct, true));
                if (ListTool.ok(nameArray)) {
                    methodWrapper = methodWrapper.and(p -> {
                        for (String key : nameArray) {
                            String keyLike = "%" + key + "%";
                            p.apply("COALESCE(name,'')||" +
//                                    "COALESCE(project_name,'')||" +
                                    "COALESCE(class_name,'') ILIKE {0}", keyLike);
//                                    "COALESCE(comment_info,'')||" +
//                                    "COALESCE(comment_scene,'')||" +
//                                    "COALESCE(comment_limit,'')||" +
//                                    "COALESCE(comment_keywords,'')
//                            .or().apply("comment_example ILIKE {0}", keyLike) // 预览页面上不直接展示的内容，不提供搜索
//                            .or().apply("comment_log ILIKE {0}", keyLike) // 预览页面上不直接展示的内容，不提供搜索
                        }
                    });
                }
                if (ListTool.ok(textArray)) {
                    methodWrapper = methodWrapper.and(p -> {
                        for (String key : textArray) {
                            String keyLike = "%" + key + "%";
                            p.apply(
//                                    "COALESCE(name,'')||" +
//                                    "COALESCE(project_name,'')||" +
//                                    "COALESCE(class_name,'')||" +
                                    "COALESCE(comment_info,'')||" +
                                            "COALESCE(comment_scene,'')||" +
                                            "COALESCE(comment_limit,'')||" +
                                            "COALESCE(comment_keywords,'') ILIKE {0}", keyLike);
//                            .or().apply("comment_example ILIKE {0}", keyLike) // 预览页面上不直接展示的内容，不提供搜索
//                            .or().apply("comment_log ILIKE {0}", keyLike) // 预览页面上不直接展示的内容，不提供搜索
                        }
                    });
                }

                IPage<JavaDocMethodLite> methodPage = javaDocMethodLiteMapper.selectPage(new Page<JavaDocMethodLite>(1, selectMethodPageSize), methodWrapper);

                methodList = methodPage.getRecords();
                // 后端关键字高亮
                // bug：导致内容被抹除
//                if (ListTool.ok(methodList)) {
//                    for (JavaDocMethodLite methodItem : methodList) {
//                        methodItem.setCommentInfo(HtmlStringTool.keywordsHightLight(HtmlStringTool.newLineBrFotmat(methodItem.getCommentInfo()), text, begTag, endTag));
//                        methodItem.setCommentScene(HtmlStringTool.keywordsHightLight(HtmlStringTool.newLineBrFotmat(methodItem.getCommentScene()), text, begTag, endTag));
//                        methodItem.setCommentLimit(HtmlStringTool.keywordsHightLight(HtmlStringTool.newLineBrFotmat(methodItem.getCommentLimit()), text, begTag, endTag));
////                        methodItem.setCommentExample(HtmlStringTool.keywordsHightLight(methodItem.getCommentExample(), text, begTag, endTag));
////                        methodItem.setCommentLog(HtmlStringTool.keywordsHightLight(methodItem.getCommentLog(), text, begTag, endTag));
//                        methodItem.setCommentKeywords(HtmlStringTool.keywordsHightLight(methodItem.getCommentKeywords(), text, begTag, endTag));
//                    }
//                }
                // 补充类信息
                if (ListTool.ok(methodList)) {
                    List<String> methodClassIdList = methodList.stream().map(JavaDocMethodLite::getClassId).distinct().collect(toList());
                    List<JavaDocClassLite> methodClassList = javaDocClassLiteMapper.selectBatchIds(methodClassIdList);

                    for (JavaDocMethodLite methodItem : methodList) {
                        for (JavaDocClassLite classItem : methodClassList) {
                            if (methodItem.getClassId().equals(classItem.getId())) {
                                methodItem.setJavaDocClassLite(JSONObject.parseObject(JSONObject.toJSONString(classItem)));
                            }
                        }
                    }
                }
            }

            JSONArray jsonArray = new JSONArray();
            if (ListTool.ok(classList)) jsonArray.addAll(classList);
            if (ListTool.ok(methodList)) jsonArray.addAll(methodList);
            responseData.putData(jsonArray);
        }
        return responseData;
    }

}
