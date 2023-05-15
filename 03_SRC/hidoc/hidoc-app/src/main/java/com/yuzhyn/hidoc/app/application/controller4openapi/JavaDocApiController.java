package com.yuzhyn.hidoc.app.application.controller4openapi;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

@Slf4j
@RestController
@RequestMapping("openapi/javadoc")
public class JavaDocApiController {


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

    /**
     * 所有项目列表
     *
     * @param params
     * @return
     */
    @PostMapping("projectList")
    public ResponseData projectList(@RequestBody Map<String, Object> params) {
        boolean existMenu = MapTool.getBoolean(params, "existMenu", false);
        List<JavaDocProject> projectList = javaDocProjectMapper.selectList(null);

        if (ListTool.ok(projectList)) {

            // 清空token信息
            for (JavaDocProject item : projectList) {
                item.setToken("");
            }

            // 只查询有菜单的项目列表
            if (existMenu) {
                List<JavaDocProject> newProjectList = new ArrayList<>();
                for (JavaDocProject project : projectList) {
                    long count = javaDocMenuMapper.selectCount(new LambdaQueryWrapper<JavaDocMenu>()
                            .eq(JavaDocMenu::getProjectId, project.getId()));
                    if (count > 0) {
                        newProjectList.add(project);
                    }
                }
                return ResponseData.okData(newProjectList);
            }
        }
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
            List<JavaDocClassLite> classList = javaDocClassLiteMapper.selectList(new LambdaQueryWrapper<JavaDocClassLite>()
                    .eq(JavaDocClassLite::getProjectId, projectId));
            if (ListTool.ok(classList)) {
                for (JavaDocClassLite classItem : classList) {
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
     * 项目中所有目录列表
     *
     * @param params
     * @return
     */
    @PostMapping("menuList")
    public ResponseData menuList(@RequestBody Map<String, Object> params) {
        String projectId = MapTool.get(params, "projectId", "").toString();
        String version = MapTool.get(params, "version", "").toString();
//        String projectName = MapTool.get(params, "projectName", "").toString();


        if (StringTool.ok(projectId)) {
            List<JavaDocMenu> menuList = javaDocMenuMapper.selectList(new LambdaQueryWrapper<JavaDocMenu>()
                    .eq(JavaDocMenu::getProjectId, projectId)
                    .orderByAsc(JavaDocMenu::getLevel));

            if (ListTool.ok(menuList)) {
                // 准备构建目录树
                for (JavaDocMenu menuItem : menuList) {
                    menuItem.setLabel(menuItem.getMenu());
                    menuItem.setChildren(new ArrayList<>());

                    for (JavaDocMenu childrenItem : menuList) {
                        if (childrenItem.getParentId().equals(menuItem.getId())) {
                            menuItem.getChildren().add(childrenItem);
                        }
                    }
                }
                menuList = menuList.stream().filter(x -> x.getLevel() == 1).collect(Collectors.toList());
                return ResponseData.okData(menuList, menuList.size());
            }
        }
        return ResponseData.error("工程不存在");
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
        String menuPath = MapTool.get(params, "menuPath", "").toString();

        LambdaQueryWrapper<JavaDocClassLite> wrapper = new LambdaQueryWrapper<JavaDocClassLite>();

        if (StringTool.ok(projectId)) {
            wrapper.and(p -> p.eq(JavaDocClassLite::getProjectId, projectId));
        }
        if (StringTool.ok(packageName)) {
            wrapper.and(p -> p.eq(JavaDocClassLite::getPackageInfo, packageName));
        }
        if (StringTool.ok(menuPath)) {
            wrapper.and(p -> p.apply("(COALESCE(comment_menu,'') ILIKE {0} OR COALESCE(comment_menu,'') = {1})", menuPath + "/%", menuPath));
        }

        ResponseData responseData = ResponseData.ok();
        List<JavaDocClassLite> classList = javaDocClassLiteMapper.selectList(wrapper);
        if (ListTool.ok(classList)) {
            responseData.putData(classList);
        }
        return responseData;
    }

    /**
     * 类详情
     *
     * @param params
     * @return
     */
    @PostMapping("classDetail")
    public ResponseData classDetail(@RequestBody Map<String, Object> params) {
        String projectId = MapTool.get(params, "projectId", "").toString();
        String classId = MapTool.get(params, "classId", "").toString();
        String version = MapTool.get(params, "version", "").toString();

        JavaDocClass classInfo = javaDocClassMapper.selectById(classId);
        List<JavaDocMethodLite> methodList = javaDocMethodLiteMapper.selectList(new LambdaQueryWrapper<JavaDocMethodLite>()
                .and(p -> p.eq(JavaDocMethodLite::getProjectId, projectId))
                .and(p -> p.eq(JavaDocMethodLite::getClassId, classId)));

        if (classInfo != null) {
            // 为每个方法设置类信息，此处会导致数据量巨大，注释掉不使用
//            for (JavaDocMethodLite methodItem : methodList) {
//                methodItem.setJavaDocClassLite(JSONObject.parseObject(JSONObject.toJSONString(classInfo)));
//            }

            ResponseData responseData = ResponseData.ok();
            responseData.putDataMap("classInfo", classInfo);

            // 如果方法列表存在数据，则一并返回
            if (ListTool.ok(methodList)) {
                responseData.putDataMap("methodList", methodList);
            }
            return responseData;
        }
        return ResponseData.error("没有查询到数据");
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
     * 上传代码包，为自动化的方式提交提供API接口，通过token验证
     *
     * @param file
     * @return
     */
    @PostMapping("uploadZip/{token}")
    public ResponseData uploadZipApi(@PathVariable String token, @RequestParam("file") MultipartFile file) {
        String zipFileName = FileTool.getNameWithoutExt(file.getOriginalFilename());
        JavaDocProject project = javaDocProjectMapper.selectOne(new LambdaQueryWrapper<JavaDocProject>()
                .eq(JavaDocProject::getName, zipFileName).eq(JavaDocProject::getToken, token));
        if (project != null) {
            return javaDocUploadService.uploadZip(file);
        }
        return ResponseData.error("项目不存在，或者token不正确");
    }

//    @PostMapping({"upload", "u"})
//    public ResponseData upload(@RequestParam(value = "projectId") String projectId, @RequestParam("file") MultipartFile[] files) {
//        return javaDocService.upload(projectId, files);
//    }

    /**
     * 查询引用信息
     *
     * @param params
     * @return
     */
    @PostMapping("queryClassByImport")
    public ResponseData queryClassByImport(@RequestBody Map<String, Object> params) {
        String importClass = MapTool.get(params, "importClass", "").toString();
        if (!StringTool.ok(importClass)) return ResponseData.error("请输入类名称");

        LambdaQueryWrapper<JavaDocClass> wrapper = new LambdaQueryWrapper<JavaDocClass>().like(JavaDocClass::getImports, "%" + importClass + "%");
        ResponseData responseData = ResponseData.ok();
        IPage<JavaDocClass> classPage = javaDocClassMapper.selectPage(new Page<JavaDocClass>(1,100),wrapper);
        if (classPage.getSize()>0) {
            responseData.putData(classPage.getRecords());
            responseData.setTotal(classPage.getTotal());
        }

        return responseData;
    }

}
