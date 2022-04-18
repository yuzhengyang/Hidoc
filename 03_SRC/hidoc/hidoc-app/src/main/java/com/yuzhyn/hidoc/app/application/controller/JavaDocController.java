package com.yuzhyn.hidoc.app.application.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.azylee.core.ios.files.FileTool;
import com.yuzhyn.hidoc.app.application.entity.javadoc.*;
import com.yuzhyn.hidoc.app.application.mapper.javadoc.*;
import com.yuzhyn.hidoc.app.application.service.javadoc.JavaDocSearchService;
import com.yuzhyn.hidoc.app.application.service.javadoc.JavaDocUploadService;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

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
    JavaDocMenuMapper javaDocMenuMapper;

    @Autowired
    JavaDocSearchService javaDocSearchService;

    @Autowired
    JavaDocUploadService javaDocUploadService;



    /**
     * 上传代码包
     *
     * @param file
     * @return
     */
    @PostMapping("uploadZip")
    public ResponseData uploadZip(@RequestParam("file") MultipartFile file) {
        return javaDocUploadService.uploadZip(file);
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
}
