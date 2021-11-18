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
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocClass;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocMethod;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocProject;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.mapper.javadoc.JavaDocClassMapper;
import com.yuzhyn.hidoc.app.application.mapper.javadoc.JavaDocMethodMapper;
import com.yuzhyn.hidoc.app.application.mapper.javadoc.JavaDocProjectMapper;
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

    @Autowired
    JavaDocService javaDocService;

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

        if (ListTool.ok(methodList)) {
            List<String> methodClassIdList = methodList.stream().map(JavaDocMethod::getClassId).distinct().collect(toList());
            List<JavaDocClass> methodClassList = javaDocClassMapper.selectBatchIds(methodClassIdList);

            for (JavaDocMethod methodItem : methodList) {
                for (JavaDocClass classItem : methodClassList) {
                    if (methodItem.getClassId().equals(classItem.getId())) {
                        methodItem.setJavaDocClass(classItem);
                    }
                }
            }
        }

        List<Object> objectList = new ArrayList<>();
        objectList.addAll(classList);
        objectList.addAll(methodList);
        responseData.putData(objectList);
        return responseData;
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

    @PostMapping({"upload", "u"})
    public ResponseData upload(@RequestParam(value = "projectId") String projectId, @RequestParam("file") MultipartFile[] files) {
        return javaDocService.upload(projectId, files);
    }

}
