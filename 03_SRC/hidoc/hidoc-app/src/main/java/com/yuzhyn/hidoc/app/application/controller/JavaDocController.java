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
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.datas.strings.HtmlStringTool;
import com.yuzhyn.azylee.core.datas.strings.StringConst;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.azylee.core.ios.files.FileFindTool;
import com.yuzhyn.azylee.core.ios.files.FileTool;
import com.yuzhyn.azylee.core.ios.zips.ZipTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.doc.Doc;
import com.yuzhyn.hidoc.app.application.entity.doc.DocHistoryLite;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.javadoc.*;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.mapper.javadoc.*;
import com.yuzhyn.hidoc.app.application.service.JavaDocService;
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


/**
 * <div javadoc="info" javadoc-cn="一句话精简概括">
 *     JavaDoc自定义文档
 * </div>
 *
 * <div javadoc="scene" javadoc-cn="使用场景说明">
 *     上传工程ZIP文件，自动解析注释，并结构话文档
 * </div>
 *
 * <div javadoc="limit" javadoc-cn="使用限制说明">
 *     限制上传zip，10M以内的文件
 * </div>
 *
 * <div javadoc="example" javadoc-cn="使用代码示例">
 *     搜索示例
 *     ```java
 *     methodWrapper = methodWrapper.and(p -> {
 *         for (String key : keywordArray) {
 *             String keyLike = "%" + key + "%";
 *             p.apply("name||class_name||comment_info||comment_scene||comment_limit||comment_keywords ILIKE {0}", keyLike);
 *         }
 *     });
 *     ```
 * </div>
 *
 * <div javadoc="log" javadoc-cn="修改日志">
 * 版本         修改时间       修改人         修改内容
 * 1.0.0.0     2021-11-20    yuzhengyang    创建功能
 * 1.0.0.1     2021-11-20    yuzhengyang    优化功能
 * </div>
 *
 * <div javadoc="keywords" javadoc-cn="搜索关键字">
 *     Hidoc，JavaDoc，自定义，解析，Java文档
 * </div>
 */
@Slf4j
@RestController
@RequestMapping("javadoc")
public class JavaDocController {

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
    JavaDocService javaDocService;

    /**
     * <div javadoc="info" javadoc-cn="一句话精简概括">
     *     搜索文档
     * </div>
     *
     * <div javadoc="scene" javadoc-cn="使用场景说明">
     *     根据关键字，搜索类、方法中包含关键字的信息
     * </div>
     *
     * <div javadoc="limit" javadoc-cn="使用限制说明">
     *     未考虑
     * </div>
     *
     * <div javadoc="example" javadoc-cn="使用代码示例">
     *     搜索示例
     *     ```java
     *     methodWrapper = methodWrapper.and(p -> {
     *         for (String key : keywordArray) {
     *             String keyLike = "%" + key + "%";
     *             p.apply("name||class_name||comment_info||comment_scene||comment_limit||comment_keywords ILIKE {0}", keyLike);
     *         }
     *     });
     *     ```
     * <p>
     *     SQL示例
     *     ```sql
     *     SELECT * FROM java_doc_class WHERE version||name ILIKE '%0%' ORDER BY SUM(version||name)
     *     ```
     * </div>
     *
     * <div javadoc="log" javadoc-cn="修改日志">
     * 版本         修改时间       修改人         修改内容
     * 1.0.0.0     2021-11-20    yuzhengyang    创建功能
     * 1.0.0.1     2021-11-20    yuzhengyang    优化功能1
     * 1.0.0.3     2021-11-20    yuzhengyang    优化功能3
     * 1.0.0.2     2021-11-20    yuzhengyang    优化功能2
     * </div>
     *
     * <div javadoc="keywords" javadoc-cn="搜索关键字">
     *     搜索
     * </div>
     */
    @PostMapping("search")
    public ResponseData search(@RequestBody Map<String, Object> params) {
        int selectClassPageSize = 100, selectMethodPageSize = 100;
        String mode = MapTool.get(params, "mode", "").toString();
        String name = MapTool.get(params, "name", "").toString();
        String text = MapTool.get(params, "text", "").toString();
        String[] nameArray = StringTool.split(name, " ", true, true);
        String[] textArray = StringTool.split(text, " ", true, true);
//        String textLike = "%" + text.replace(' ', '%') + "%"; // 替换空格为通配符有局限性，比如有强制的前后顺序
        String begTag = "<span style='color:red;'>";
        String endTag = "</span>";

        ResponseData responseData = ResponseData.ok();
        List<JavaDocProject> projectList = javaDocProjectMapper.selectList(null);

        if (ListTool.ok(projectList)) {
            List<String> projectVersionList = projectList.stream().map(JavaDocProject::getVersion).distinct().collect(toList());
            List<JavaDocClassLite> classList = null;
            List<JavaDocMethodLite> methodList = null;

            if (mode.equals("class") || mode.equals("all")) {

                LambdaQueryWrapper<JavaDocClassLite> classWrapper = new LambdaQueryWrapper<JavaDocClassLite>();
                classWrapper = classWrapper.and(p -> p.in(JavaDocClassLite::getVersion, projectVersionList));
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
                if (ListTool.ok(classList)) {
                    for (JavaDocClassLite classItem : classList) {
                        classItem.setCommentInfo(HtmlStringTool.keywordsHightLight(HtmlStringTool.newLineBrFotmat(classItem.getCommentInfo()), text, begTag, endTag));
                        classItem.setCommentScene(HtmlStringTool.keywordsHightLight(HtmlStringTool.newLineBrFotmat(classItem.getCommentScene()), text, begTag, endTag));
                        classItem.setCommentLimit(HtmlStringTool.keywordsHightLight(HtmlStringTool.newLineBrFotmat(classItem.getCommentLimit()), text, begTag, endTag));
//                        classItem.setCommentExample(HtmlStringTool.keywordsHightLight(classItem.getCommentExample(), text, begTag, endTag));
//                        classItem.setCommentLog(HtmlStringTool.keywordsHightLight(classItem.getCommentLog(), text, begTag, endTag));
                        classItem.setCommentKeywords(HtmlStringTool.keywordsHightLight(classItem.getCommentKeywords(), text, begTag, endTag));
                    }
                }
            }
            if (mode.equals("method") || mode.equals("all")) {

                LambdaQueryWrapper<JavaDocMethodLite> methodWrapper = new LambdaQueryWrapper<JavaDocMethodLite>();
                methodWrapper = methodWrapper.and(p -> p.in(JavaDocMethodLite::getVersion, projectVersionList));
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
                if (ListTool.ok(methodList)) {
                    for (JavaDocMethodLite methodItem : methodList) {
                        methodItem.setCommentInfo(HtmlStringTool.keywordsHightLight(HtmlStringTool.newLineBrFotmat(methodItem.getCommentInfo()), text, begTag, endTag));
                        methodItem.setCommentScene(HtmlStringTool.keywordsHightLight(HtmlStringTool.newLineBrFotmat(methodItem.getCommentScene()), text, begTag, endTag));
                        methodItem.setCommentLimit(HtmlStringTool.keywordsHightLight(HtmlStringTool.newLineBrFotmat(methodItem.getCommentLimit()), text, begTag, endTag));
//                        methodItem.setCommentExample(HtmlStringTool.keywordsHightLight(methodItem.getCommentExample(), text, begTag, endTag));
//                        methodItem.setCommentLog(HtmlStringTool.keywordsHightLight(methodItem.getCommentLog(), text, begTag, endTag));
                        methodItem.setCommentKeywords(HtmlStringTool.keywordsHightLight(methodItem.getCommentKeywords(), text, begTag, endTag));
                    }
                }
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

    @PostMapping("getOriginalDocument")
    public ResponseData getOriginalDocument(@RequestBody Map<String, Object> params) {
        String classId = MapTool.get(params, "classId", "").toString();
        JavaDocClass javaDocClass = javaDocClassMapper.selectById(classId);
        if (javaDocClass != null) {
            return ResponseData.okData("originalDocument", javaDocClass.getOriginalDocument());
        }
        return ResponseData.error("没有找到类源文件");
    }

    @PostMapping("getSourceCode")
    public ResponseData getSourceCode(@RequestBody Map<String, Object> params) {
        String type = MapTool.get(params, "type", "").toString();
        String id = MapTool.get(params, "id", "").toString();
        switch (type) {
            case "class":
                JavaDocClass javaDocClass = javaDocClassMapper.selectById(id);
                if (javaDocClass != null) {
                    // 这里其实没有使用，这里的内容是文件内容，并不是严格的类源代码
                    return ResponseData.okData("sourceCode", javaDocClass.getOriginalDocument());
                }
                break;
            case "method":
                JavaDocMethod method = javaDocMethodMapper.selectById(id);
                if (method != null) {
                    return ResponseData.okData("sourceCode", method.getSourceCode());
                }
                break;
            default:
                break;
        }
        return ResponseData.error("没有找到源代码信息");
    }


    @PostMapping("projectList")
    public ResponseData projectList(@RequestBody Map<String, Object> params) {
        List<JavaDocProject> projectList = javaDocProjectMapper.selectList(null);
        return ResponseData.okData(projectList);
    }

    @PostMapping("uploadZip")
    public ResponseData uploadZip(@RequestParam("file") MultipartFile file) {
        ResponseData responseData;
        List<String> step = new ArrayList<>();
        try {
            step.add("创建临时文件目录");
            String tempPath = DirTool.combine(R.Paths.Temp, DateTimeFormat.toStr(LocalDateTime.now(), DateTimeFormatPattern.SHORT_DATE));
            DirTool.create(tempPath);

            step.add("临时存放上传的zip文件");
            String fileNameNoExt = R.SnowFlake.nexts();
            String tempFilePath = DirTool.combine(tempPath, fileNameNoExt + ".zip");
            java.io.File dest = new java.io.File(tempFilePath);
            file.transferTo(dest);

            step.add("解压缩zip");
            ZipTool.unzip(tempFilePath, tempPath, fileNameNoExt);

            step.add("搜索所有java文件");
            List<String> javaFileList = FileFindTool.getAllFiles(DirTool.combine(tempPath, fileNameNoExt), ".java");

            // 搜索并查找project信息
            if (ListTool.ok(javaFileList)) {
                String zipFileName = FileTool.getNameWithoutExt(file.getOriginalFilename());
                responseData = javaDocService.uploadZip(zipFileName, javaFileList);

                // 打扫现场，删除临时文件
                if (tempFilePath.startsWith(R.Paths.Temp)) FileTool.delete(tempFilePath);
                if (tempPath.startsWith(R.Paths.Temp)) FileTool.delete(DirTool.combine(tempPath, fileNameNoExt));

                responseData.putDataMap("step", step);
                return responseData;
            } else {
                responseData = ResponseData.error("没有找到java文件");
                responseData.putDataMap("step", step);
                return responseData;
            }
        } catch (Exception ex) {
            responseData = ResponseData.error("zip文件分析失败");
            responseData.putDataMap("step", step);
            return responseData;
        }
    }

//    @PostMapping({"upload", "u"})
//    public ResponseData upload(@RequestParam(value = "projectId") String projectId, @RequestParam("file") MultipartFile[] files) {
//        return javaDocService.upload(projectId, files);
//    }

}
