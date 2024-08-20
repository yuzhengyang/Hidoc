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
import java.util.*;

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

    @Autowired
    JavaDocQueryLogMapper javaDocQueryLogMapper;

    @Autowired
    JavaDocHelpfulMapper javaDocHelpfulMapper;

    public ResponseData search(Map<String, Object> params) {

        String mode = MapTool.get(params, "mode", "").toString();
        String name = MapTool.get(params, "name", "").toString();
        String text = MapTool.get(params, "text", "").toString();
        Boolean haveScene = MapTool.getBoolean(params, "haveScene", "false");

        String[] projectArray;
        if (MapTool.get(params, "projects", null) != null) {
            JSONArray projects = (JSONArray) MapTool.get(params, "projects", null);
            if (projects != null && !projects.isEmpty()) {
                projectArray = projects.toArray(new String[0]);
            } else {
                projectArray = null;
            }
        } else {
            projectArray = null;
        }
        String[] nameArray = split(name, " ", true, true);
        String[] textArray = split(text, " ", true, true);

        saveQueryLog(mode, nameArray, textArray, projectArray);

        Set<String> highlightKey = new HashSet<>();
        if (nameArray != null) highlightKey.addAll(Arrays.asList(nameArray));
        if (textArray != null) highlightKey.addAll(Arrays.asList(textArray));

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
        if (!ListTool.ok(projectList)) return responseData;

        List<JavaDocClassLite> classList = null;
        List<JavaDocMethodLite> methodList = null;

        if (mode.equals("class") || mode.equals("all")) {

            LambdaQueryWrapper<JavaDocClassLite> classWrapper = new LambdaQueryWrapper<JavaDocClassLite>();
            classWrapper = classWrapper.and(q -> q.eq(JavaDocClassLite::getIsStruct, true));
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
            if (haveScene) {
                classWrapper = classWrapper.and(q -> q.ne(JavaDocClassLite::getCommentScene, ""));
            }
            if (ListTool.ok(projectArray)) {
                classWrapper = classWrapper.and(q -> q.in(JavaDocClassLite::getProjectName, projectArray));
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
            if (ListTool.ok(classList)) {
                for (JavaDocClassLite classItem : classList) {
                    classItem.set_highlightKeys(highlightKey.toArray(new String[0]));
//                        classItem.setCommentInfo(HtmlStringTool.keywordsHightLight(HtmlStringTool.newLineBrFotmat(classItem.getCommentInfo()), text, begTag, endTag));
//                        classItem.setCommentScene(HtmlStringTool.keywordsHightLight(HtmlStringTool.newLineBrFotmat(classItem.getCommentScene()), text, begTag, endTag));
//                        classItem.setCommentLimit(HtmlStringTool.keywordsHightLight(HtmlStringTool.newLineBrFotmat(classItem.getCommentLimit()), text, begTag, endTag));
////                        classItem.setCommentExample(HtmlStringTool.keywordsHightLight(classItem.getCommentExample(), text, begTag, endTag));
////                        classItem.setCommentLog(HtmlStringTool.keywordsHightLight(classItem.getCommentLog(), text, begTag, endTag));
//                        classItem.setCommentKeywords(HtmlStringTool.keywordsHightLight(classItem.getCommentKeywords(), text, begTag, endTag));
                }
            }
        }
        if (mode.equals("method") || mode.equals("all")) {

            LambdaQueryWrapper<JavaDocMethodLite> methodWrapper = new LambdaQueryWrapper<JavaDocMethodLite>();
            methodWrapper = methodWrapper.and(q -> q.eq(JavaDocMethodLite::getIsStruct, true));
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
            if (haveScene) {
                methodWrapper = methodWrapper.and(q -> q.ne(JavaDocMethodLite::getCommentScene, ""));
            }
            if (ListTool.ok(projectArray)) {
                methodWrapper = methodWrapper.and(q -> q.in(JavaDocMethodLite::getProjectName, projectArray));
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
                    methodItem.set_highlightKeys(highlightKey.toArray(new String[0]));

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
        return responseData;
    }

    private void saveQueryLog(String mode, String[] nameArray, String[] textArray, String[] projectArray) {
        String traceId = R.SnowFlake.nexts();
        R.Queues.JavaDocQueryLogQueue.add(new JavaDocQueryLog(traceId, "mode", mode));
        if (ListTool.ok(nameArray)) {
            for (String s : nameArray) {
                R.Queues.JavaDocQueryLogQueue.add(new JavaDocQueryLog(traceId, "name", s));
            }
        } else {
            R.Queues.JavaDocQueryLogQueue.add(new JavaDocQueryLog(traceId, "name", "*"));
        }
        if (ListTool.ok(textArray)) {
            for (String s : textArray) {
                R.Queues.JavaDocQueryLogQueue.add(new JavaDocQueryLog(traceId, "text", s));
            }
        } else {
            R.Queues.JavaDocQueryLogQueue.add(new JavaDocQueryLog(traceId, "text", "*"));
        }
        if (ListTool.ok(projectArray)) {
            for (String s : projectArray) {
                R.Queues.JavaDocQueryLogQueue.add(new JavaDocQueryLog(traceId, "project", s));
            }
        } else {
            R.Queues.JavaDocQueryLogQueue.add(new JavaDocQueryLog(traceId, "project", "*"));
        }
    }

    public ResponseData helpful(Map<String, Object> params) {
        String metaId = MapTool.get(params, "metaId", "").toString();
        String classId = MapTool.get(params, "classId", "").toString();
        String projectId = MapTool.get(params, "projectId", "").toString();
        Boolean isHelpful = MapTool.getBoolean(params, "isHelpful", true);

        JavaDocHelpful helpful = new JavaDocHelpful(metaId, classId, projectId, isHelpful);
        javaDocHelpfulMapper.insert(helpful);

        return ResponseData.ok();
    }

    /**
     * 这里由于公共工具代码异常，暂时在这里修复，等到公共代码发布之后，使用新的公共工具版本库
     *
     * @param s
     * @param regex
     * @param filterSpace
     * @param filterRepeat
     * @return
     */
    public static String[] split(String s, String regex, boolean filterSpace, boolean filterRepeat) {
        if (StringTool.ok(s)) {
            String[] array = s.split(regex);
            // 过滤空格
            List<String> list = new ArrayList<String>();
            Set<String> sets = new HashSet<String>();
            for (String item : array) {
                boolean canAdd = false;

                if (filterSpace) {
                    if (StringTool.ok(item.trim())) canAdd = true;
                } else {
                    canAdd = true;
                }

                if (canAdd) {
                    if (filterRepeat) {
                        if (!sets.contains(item)) {
                            list.add(item);
                            sets.add(item);
                        }
                    } else {
                        list.add(item);
                    }
                }
            }
            return list.toArray(new String[list.size()]);
        } else {
            return null;
        }
    }

}
