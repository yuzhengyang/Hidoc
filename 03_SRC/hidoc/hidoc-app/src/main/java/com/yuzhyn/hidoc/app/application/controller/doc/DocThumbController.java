package com.yuzhyn.hidoc.app.application.controller.doc;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.application.entity.doc.DocComment;
import com.yuzhyn.hidoc.app.application.entity.doc.DocThumb;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocThumbMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("docthumb")
public class DocThumbController {

    @Autowired
    DocThumbMapper docThumbMapper;

    @Autowired
    SysUserLiteMapper sysUserLiteMapper;

    @PostMapping("create")
    public ResponseData create(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "tableName", "dataId", "type", "value")) {
            String tableName = MapTool.get(params, "tableName", "").toString();
            String dataId = MapTool.get(params, "dataId", "").toString();
            String type = MapTool.get(params, "type", "").toString();
            Boolean value = MapTool.getBoolean(params, "value", false);

            if (StringTool.ok(tableName, dataId, type)) {

                Boolean isSupporter = false, isProtester = false;
                if (type.equals("supporter") && value) isSupporter = value;
                if (type.equals("protester") && value) isProtester = value;

                String userId = CurrentUserManager.getUser().getId();
                String thumbsId = DocThumb.genId(tableName, dataId, userId);
                DocThumb docThumb = docThumbMapper.selectById(thumbsId);
                if (docThumb == null) {
                    docThumb = new DocThumb();
                    docThumb.setId(thumbsId);
                    docThumb.setTableName(tableName);
                    docThumb.setDataId(dataId);
                    docThumb.setUserId(userId);
                    docThumb.setCreateTime(LocalDateTime.now());
                    docThumb.setUpdateTime(null);
                    docThumb.setIsSupporter(isSupporter);
                    docThumb.setIsProtester(isProtester);
                    docThumbMapper.insert(docThumb);
                } else {
                    docThumb.setUpdateTime(LocalDateTime.now());
                    docThumb.setIsSupporter(isSupporter);
                    docThumb.setIsProtester(isProtester);
                    docThumbMapper.updateById(docThumb);
                }

                // 点赞情况查询
                long thumbCount = docThumbMapper.selectCount(new LambdaQueryWrapper<DocThumb>()
                        .eq(DocThumb::getTableName, tableName)
                        .eq(DocThumb::getDataId, dataId)
                        .eq(DocThumb::getIsSupporter, true));

                ResponseData responseData = ResponseData.ok();
                responseData.putDataMap("thumbCount", thumbCount);
                responseData.putDataMap("myThumb", docThumb);

                return responseData;
            }
        }
        return ResponseData.error("缺少关键信息");
    }


    @PostMapping("get")
    public ResponseData get(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "tableName", "dataId")) {
            String tableName = MapTool.get(params, "tableName", "").toString();
            String dataId = MapTool.get(params, "dataId", "").toString();

            if (StringTool.ok(tableName, dataId)) {
                String userId = CurrentUserManager.getUser().getId();
                String thumbsId = DocThumb.genId(tableName, dataId, userId);
                DocThumb docThumb = docThumbMapper.selectById(thumbsId);

                return ResponseData.okData("docThumb", docThumb);
            }
        }
        return ResponseData.error("缺少关键信息");
    }
}
