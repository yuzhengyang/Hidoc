package com.yuzhyn.hidoc.app.application.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.azylee.core.ios.files.FileTool;
import com.yuzhyn.hidoc.app.application.entity.javadoc.*;
import com.yuzhyn.hidoc.app.application.entity.team.Team;
import com.yuzhyn.hidoc.app.application.entity.team.TeamLite;
import com.yuzhyn.hidoc.app.application.entity.team.TeamMember;
import com.yuzhyn.hidoc.app.application.mapper.javadoc.*;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMemberMapper;
import com.yuzhyn.hidoc.app.application.service.javadoc.JavaDocSearchService;
import com.yuzhyn.hidoc.app.application.service.javadoc.JavaDocUploadService;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
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
    JavaDocMetaMapper javaDocMetaMapper;

    @Autowired
    TeamLiteMapper teamLiteMapper;

    @Autowired
    JavaDocMetaLiteMapper javaDocMetaLiteMapper;

    @Autowired
    JavaDocMenuMapper javaDocMenuMapper;

    @Autowired
    TeamMemberMapper teamMemberMapper;

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

        // 循环查询工程的搜索权限和代码查看权限，属于哪个团队
        if (ListTool.ok(projectList)) {
            // 先根据名称排序
            projectList.sort(Comparator.comparing(JavaDocProject::getName));

            // 把涉及到的团队信息都一次性查询出来，放到map里面，后续使用，避免循环查询
            Map<String, TeamLite> teamMap = new HashMap<>();
            List<String> teamsList = new ArrayList<>();
            teamsList.addAll(projectList.stream().map(JavaDocProject::getTeamsRead).toList());
            teamsList.addAll(projectList.stream().map(JavaDocProject::getTeamsCode).toList());
            Set<String> teamIds = new HashSet<>();
            for (String teams : teamsList) {
                String[] teamsArray = StringTool.split(teams, ",", true, true, true);
                if (ListTool.ok(teamsArray)) {
                    teamIds.addAll(Arrays.asList(teamsArray));
                }
            }
            if (ListTool.ok(teamIds)) {
                List<TeamLite> teamList = teamLiteMapper.selectList(new LambdaQueryWrapper<TeamLite>().in(TeamLite::getId, teamIds));
                teamMap = teamList.stream().collect(Collectors.toMap(TeamLite::getId, team -> team));
            }
            // 根据工程的团队权限设置，给赋值上对应的团队信息
            for (JavaDocProject project : projectList) {
                project.setTeamsReadList(new ArrayList<>());
                project.setTeamsCodeList(new ArrayList<>());
                String teamsCodeString = project.getTeamsCode();
                String teamsReadString = project.getTeamsRead();
                String[] teamsCodeArray = StringTool.split(teamsCodeString, ",", true, true, true);
                String[] teamsReadArray = StringTool.split(teamsReadString, ",", true, true, true);
                if (ListTool.ok(teamsCodeArray)) {
                    for (String teamId : teamsCodeArray) {
                        if (teamMap.containsKey(teamId)) {
                            TeamLite team = teamMap.get(teamId);
                            project.getTeamsCodeList().add(new TeamLite(team.getId(), team.getName()));
                        }
                    }
                }
                if (ListTool.ok(teamsReadArray)) {
                    for (String teamId : teamsReadArray) {
                        if (teamMap.containsKey(teamId)) {
                            TeamLite team = teamMap.get(teamId);
                            project.getTeamsReadList().add(new TeamLite(team.getId(), team.getName()));
                        }
                    }
                }
            }
        }
        return ResponseData.okData(projectList);
    }

    @PostMapping("getOriginalDocument")
    public ResponseData getOriginalDocument(@RequestBody Map<String, Object> params) {
        String classId = MapTool.get(params, "classId", "").toString();
        JavaDocMeta javaDocClass = javaDocMetaMapper.selectById(classId);
        if (javaDocClass != null) {

            // 校验代码查看权限
            JavaDocProject project = javaDocProjectMapper.selectById(javaDocClass.getProjectId());
            List<TeamMember> teamMemberList = teamMemberMapper.selectList(new LambdaQueryWrapper<TeamMember>().eq(TeamMember::getUserId, CurrentUserManager.getUserId()));
            if (StringTool.ok(project.getTeamsCode())) {
                if (ListTool.ok(teamMemberList)) {
                    for (TeamMember member : teamMemberList) {
                        if (project.getTeamsCode().contains(member.getTeamId())) {
                            return ResponseData.okData("originalDocument", javaDocClass.getSourceCode());
                        }
                    }
                }
                return ResponseData.error("您不属于该工程源码查看的团队，不能查看源码内容");
            } else {
                return ResponseData.okData("originalDocument", javaDocClass.getSourceCode());
            }
        }
        return ResponseData.error("没有找到类源文件");
    }

    @PostMapping("getSourceCode")
    public ResponseData getSourceCode(@RequestBody Map<String, Object> params) {
        String type = MapTool.get(params, "type", "").toString();
        String id = MapTool.get(params, "id", "").toString();

        JavaDocMeta method = javaDocMetaMapper.selectById(id);
        if (method != null) {

            // 校验代码查看权限
            JavaDocProject project = javaDocProjectMapper.selectById(method.getProjectId());
            List<TeamMember> teamMemberList = teamMemberMapper.selectList(new LambdaQueryWrapper<TeamMember>().eq(TeamMember::getUserId, CurrentUserManager.getUserId()));
            if (StringTool.ok(project.getTeamsCode())) {
                if (ListTool.ok(teamMemberList)) {
                    for (TeamMember member : teamMemberList) {
                        if (project.getTeamsCode().contains(member.getTeamId())) {
                            return ResponseData.okData("sourceCode", method.getSourceCode());
                        }
                    }
                }
                return ResponseData.error("您不属于该工程源码查看的团队，不能查看源码内容");
            } else {
                return ResponseData.okData("sourceCode", method.getSourceCode());
            }
        }
        return ResponseData.error("没有找到源代码信息");
    }
}
