package com.yuzhyn.hidoc.app.application.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.azylee.core.ios.files.FileFindTool;
import com.yuzhyn.azylee.core.ios.files.FileTool;
import com.yuzhyn.azylee.core.ios.zips.ZipTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.javadoc.*;
import com.yuzhyn.hidoc.app.application.mapper.javadoc.*;
import com.yuzhyn.hidoc.app.application.service.javadoc.JavaDocCreateService;
import com.yuzhyn.hidoc.app.application.service.javadoc.JavaDocSearchService;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

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
    JavaDocCreateService javaDocCreateService;

    @Autowired
    JavaDocSearchService javaDocSearchService;

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
        return javaDocSearchService.search(params);
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


    /**
     * 所有项目列表
     *
     * @param params
     * @return
     */
    @PostMapping("projectList")
    public ResponseData projectList(@RequestBody Map<String, Object> params) {
        List<JavaDocProject> projectList = javaDocProjectMapper.selectList(null);
        return ResponseData.okData(projectList);
    }

    /**
     * 项目中所有包列表
     *
     * @param params
     * @return
     */
    @PostMapping("packageList")
    public ResponseData packageList(@RequestBody Map<String, Object> params) {
        String projectId = MapTool.get(params, "projectId", "").toString();
        String version = MapTool.get(params, "version", "").toString();
        String projectName = MapTool.get(params, "projectName", "").toString();

        Set<String> packages = new HashSet<>();
        if (StringTool.ok(projectId)) {
            List<JavaDocClass> classList = javaDocClassMapper.selectList(new LambdaQueryWrapper<JavaDocClass>()
                    .eq(JavaDocClass::getProjectId, projectId)
                    .eq(JavaDocClass::getVersion, version));
            if (ListTool.ok(classList)) {
                for (JavaDocClass classItem : classList) {
                    if (StringTool.ok(classItem.getPackageInfo())) packages.add(classItem.getPackageInfo());
                }
            }
        }
        ResponseData responseData = ResponseData.ok();
        List<String> packageList = Arrays.asList(packages.toArray(new String[0]));
        Collections.sort(packageList);
        responseData.putData(packageList);
        return responseData;
    }

    /**
     * 项目中类列表
     *
     * @param params
     * @return
     */
    @PostMapping("classList")
    public ResponseData classList(@RequestBody Map<String, Object> params) {
        String projectId = MapTool.get(params, "projectId", "").toString();
        String version = MapTool.get(params, "version", "").toString();
        String packageName = MapTool.get(params, "packageName", "").toString();

        LambdaQueryWrapper<JavaDocClassLite> wrapper = new LambdaQueryWrapper<JavaDocClassLite>();
        wrapper.and(p -> p.eq(JavaDocClassLite::getVersion, version));

        if (StringTool.ok(projectId)) {
            wrapper.and(p -> p.eq(JavaDocClassLite::getProjectId, projectId));
        }
        if (StringTool.ok(packageName)) {
            wrapper.and(p -> p.eq(JavaDocClassLite::getPackageInfo, packageName));
        }

        ResponseData responseData = ResponseData.ok();
        List<JavaDocClassLite> classList = javaDocClassLiteMapper.selectList(wrapper);
        if (ListTool.ok(classList)) {
            responseData.putData(classList);
        }
        return responseData;
    }


    /**
     * 类中的方法列表
     *
     * @param params
     * @return
     */
    @PostMapping("methodList")
    public ResponseData methodList(@RequestBody Map<String, Object> params) {
        String projectId = MapTool.get(params, "projectId", "").toString();
        String classId = MapTool.get(params, "classId", "").toString();
        String version = MapTool.get(params, "version", "").toString();

        LambdaQueryWrapper<JavaDocMethodLite> wrapper = new LambdaQueryWrapper<JavaDocMethodLite>();
        wrapper.and(p -> p.eq(JavaDocMethodLite::getVersion, version));

        if (StringTool.ok(projectId)) {
            wrapper.and(p -> p.eq(JavaDocMethodLite::getProjectId, projectId));
        }
        if (StringTool.ok(classId)) {
            wrapper.and(p -> p.eq(JavaDocMethodLite::getClassId, classId));
        }

        ResponseData responseData = ResponseData.ok();
        List<JavaDocMethodLite> methodList = javaDocMethodLiteMapper.selectList(wrapper);
        if (ListTool.ok(methodList)) {

            // 补充类信息
            List<String> methodClassIdList = methodList.stream().map(JavaDocMethodLite::getClassId).distinct().collect(toList());
            List<JavaDocClassLite> methodClassList = javaDocClassLiteMapper.selectBatchIds(methodClassIdList);

            for (JavaDocMethodLite methodItem : methodList) {
                for (JavaDocClassLite classItem : methodClassList) {
                    if (methodItem.getClassId().equals(classItem.getId())) {
                        methodItem.setJavaDocClassLite(JSONObject.parseObject(JSONObject.toJSONString(classItem)));
                    }
                }
            }

            responseData.putData(methodList);
        }
        return responseData;
    }

    /**
     * 上传代码包
     *
     * @param file
     * @return
     */
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
                responseData = javaDocCreateService.uploadZip(zipFileName, javaFileList);

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
