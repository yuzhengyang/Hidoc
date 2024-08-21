package com.yuzhyn.hidoc.app.application.service.javadoc;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocMenu;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocMetaLite;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocProject;
import com.yuzhyn.hidoc.app.application.mapper.javadoc.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class JavaDocCleanService {

    @Autowired
    JavaDocProjectMapper javaDocProjectMapper;

    @Autowired
    JavaDocMetaLiteMapper  javaDocMetaLiteMapper;

    @Autowired
    JavaDocMenuMapper javaDocMenuMapper;

    public void cleanJavaDocData(String projectName) {

        try {
            JavaDocProject project = javaDocProjectMapper.selectOne(new LambdaQueryWrapper<JavaDocProject>()
                    .eq(JavaDocProject::getName, projectName));
            if (project != null) {
                int classFlag = javaDocMetaLiteMapper.delete(new LambdaQueryWrapper<JavaDocMetaLite>()
                        .eq(JavaDocMetaLite::getProjectId, project.getId()));

                int menuFlag = javaDocMenuMapper.delete(new LambdaQueryWrapper<JavaDocMenu>()
                        .eq(JavaDocMenu::getProjectId, project.getId()));

                log.info("删除历史信息：meta：" + classFlag +  "，menu：" + menuFlag + "。");
            }
        } catch (Exception ex) {
        }

    }
}
