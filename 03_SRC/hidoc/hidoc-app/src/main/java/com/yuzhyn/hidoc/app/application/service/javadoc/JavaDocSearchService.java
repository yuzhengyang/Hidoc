package com.yuzhyn.hidoc.app.application.service.javadoc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
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
import com.yuzhyn.hidoc.app.application.entity.doc.DocThumb;
import com.yuzhyn.hidoc.app.application.entity.javadoc.*;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.mapper.javadoc.*;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
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
    JavaDocMetaMapper javaDocMetaMapper;

    @Autowired
    JavaDocMetaSearchLiteMapper javaDocMetaSearchLiteMapper;

    @Autowired
    JavaDocMetaLiteMapper javaDocMetaLiteMapper;

    @Autowired
    JavaDocQueryLogMapper javaDocQueryLogMapper;

    @Autowired
    JavaDocHelpfulMapper javaDocHelpfulMapper;

    public ResponseData search(Map<String, Object> params) {

        String metaType = MapTool.get(params, "metaType", "").toString();
        String name = MapTool.get(params, "name", "").toString();
        String text = MapTool.get(params, "text", "").toString();
        Boolean isStruct = MapTool.getBoolean(params, "isStruct", "false");

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

        saveQueryLog(metaType, nameArray, textArray, projectArray);

        Set<String> highlightKey = new HashSet<>();
        if (nameArray != null) highlightKey.addAll(Arrays.asList(nameArray));
        if (textArray != null) highlightKey.addAll(Arrays.asList(textArray));

        // 不输入任何信息，直接搜索是没有意义的，这里限制搜索个数，仅做示意
        Long offset = 0L;
        Long limit = 10L;
        if (StringTool.ok(name) || StringTool.ok(text)) {
            limit = 50L;
        }

        ResponseData responseData = ResponseData.ok();
        List<JavaDocProject> projectList = javaDocProjectMapper.selectList(null);
        if (!ListTool.ok(projectList)) return responseData;

        LambdaQueryWrapper<JavaDocMetaLite> classWrapper = new LambdaQueryWrapper<JavaDocMetaLite>();
        if(isStruct){
            classWrapper = classWrapper.and(q -> q.eq(JavaDocMetaLite::getIsStruct, true));
        }
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
        if (ListTool.ok(projectArray)) {
            classWrapper = classWrapper.and(q -> q.in(JavaDocMetaLite::getProjectName, projectArray));
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
        List<JavaDocMetaSearchLite> classList   = javaDocMetaSearchLiteMapper.selectList(classWrapper, limit, offset);

        if (ListTool.ok(classList)) {

            List<String> methodClassIdList = classList.stream().filter(x -> x.getClassId() != null).map(JavaDocMetaSearchLite::getClassId).distinct().collect(toList());
            List<JavaDocMetaLite> methodClassList = null;
            if (ListTool.ok(methodClassIdList)) methodClassList = javaDocMetaLiteMapper.selectBatchIds(methodClassIdList);

            for (JavaDocMetaSearchLite methodItem : classList) {
                // 设置高亮的关键字，交给前端去渲染高亮
                methodItem.set_highlightKeys(highlightKey.toArray(new String[0]));

                if (ListTool.ok(methodClassList)) {
                    for (JavaDocMetaLite classItem : methodClassList) {
                        if (methodItem.getClassId() != null && methodItem.getClassId().equals(classItem.getId())) {
                            methodItem.setJavaDocClassLite(JSONObject.parseObject(JSONObject.toJSONString(classItem)));
                        }
                    }
                }
            }
        }

        JSONArray jsonArray = new JSONArray();
        if (ListTool.ok(classList)) jsonArray.addAll(classList);
        responseData.putData(jsonArray);
        return responseData;
    }

    private void saveQueryLog(String metaType, String[] nameArray, String[] textArray, String[] projectArray) {
        String traceId = R.SnowFlake.nexts();
        R.Queues.JavaDocQueryLogQueue.add(new JavaDocQueryLog(traceId, "metaType", metaType));
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
        Double helpfulRate = MapTool.getDouble(params, "helpfulRate", 0);

        JavaDocHelpful helpful = new JavaDocHelpful(metaId, classId, projectId, helpfulRate);
        JavaDocHelpful row = javaDocHelpfulMapper.selectById(helpful.getId());

        if (row == null) {
            javaDocHelpfulMapper.insert(helpful);
        } else {
            javaDocHelpfulMapper.updateById(helpful);
        }
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
