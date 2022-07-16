package com.yuzhyn.hidoc.app.application.controller4openapi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.application.entity.doc.DocThumb;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLiteOnline;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocThumbMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteOnlineMapper;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("openapi/docthumb")
public class DocThumbApiController {

    @Autowired
    DocThumbMapper docThumbMapper;

    @Autowired
    SysUserLiteMapper sysUserLiteMapper;

    @PostMapping("userList")
    public ResponseData userList(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        if (MapTool.ok(params, "tableName", "dataId")) {
            String tableName = MapTool.get(params, "tableName", "").toString();
            String dataId = MapTool.get(params, "dataId", "").toString();
            String userId = "";
            if (CurrentUserManager.isLogin()) {
                userId = CurrentUserManager.getUserId();
            }

            if (StringTool.ok(tableName, dataId)) {
                List<DocThumb> docThumbList = docThumbMapper.selectList(new LambdaQueryWrapper<DocThumb>()
                        .eq(DocThumb::getIsSupporter, true)
                        .eq(DocThumb::getTableName, tableName)
                        .eq(DocThumb::getDataId, dataId).orderByAsc(DocThumb::getCreateTime));

                if (ListTool.ok(docThumbList)) {
                    responseData.putDataMap("docThumbList", docThumbList);

                    List<String> userIds = new ArrayList<>();
                    List<String> _uid = docThumbList.stream().map(DocThumb::getUserId).distinct().collect(toList());
                    if (StringTool.ok(userId) && _uid.contains(userId)) {
                        userIds.add(userId);
                        _uid.remove(userId);
                    }
                    userIds.addAll(_uid);

                    List<SysUserLite> dbUserList = sysUserLiteMapper.selectBatchIds(userIds);
                    List<SysUserLite> userLiteList = new ArrayList<>();

                    if (ListTool.ok(dbUserList)) {
                        for (String id : userIds) {
                            for (SysUserLite lite : dbUserList) {
                                if (id.equals(lite.getId())) {
                                    userLiteList.add(lite);
                                    break;
                                }
                            }
                        }
                    }

                    if (ListTool.ok(userLiteList)) {
                        responseData.putDataMap("userLiteList", userLiteList);
                    }
                }

            }
        }
        return responseData;
    }
}
