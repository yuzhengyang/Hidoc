package com.yuzhyn.hidoc.app.application.controller4openapi;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.application.entity.doc.DocComment;
import com.yuzhyn.hidoc.app.application.entity.doc.DocThumb;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocCommentMapper;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("openapi/doccomment")
public class DocCommentApiController {


    @Autowired
    DocCommentMapper docCommentMapper;

    @Autowired
    DocLiteMapper docLiteMapper;

    @Autowired
    SysUserLiteMapper sysUserLiteMapper;

    @PostMapping("list")
    public ResponseData list(@RequestBody Map<String, Object> params) {
        // 获取参数信息
        String docId = MapTool.get(params, "docId", "").toString();

        // 配置查询条件
        LambdaQueryWrapper<DocComment> lambdaQueryWrapper = new LambdaQueryWrapper<DocComment>();
        lambdaQueryWrapper = lambdaQueryWrapper.eq(DocComment::getDocId, docId).eq(DocComment::getIsDelete, false).orderByDesc(DocComment::getCreateTime);
        List<DocComment> commentList = docCommentMapper.selectList(lambdaQueryWrapper);

        if (ListTool.ok(commentList)) {
            List<String> userIds = new ArrayList<>();
            List<String> userIds1 = commentList.stream().map(DocComment::getCreateUserId).distinct().collect(toList());
            List<String> userIds2 = commentList.stream().map(DocComment::getReplyUserId).distinct().collect(toList());
            userIds.addAll(userIds1);
            userIds.addAll(userIds2);
            List<SysUserLite> userLites = sysUserLiteMapper.selectBatchIds(userIds);
            if (ListTool.ok(userLites)) {
                for (SysUserLite userItem : userLites) {
                    for (DocComment commentItem : commentList) {
                        if (commentItem.getCreateUserId().equals(userItem.getId())) {
                            commentItem.setCreateUser(JSONObject.parseObject(JSONObject.toJSONString(userItem)));
                        }
                        if (commentItem.getReplyUserId().equals(userItem.getId())) {
                            commentItem.setReplyUser(JSONObject.parseObject(JSONObject.toJSONString(userItem)));
                        }
                        if (commentItem.getCreateUser() != null && commentItem.getReplyUser() != null) {
                            continue;
                        }
                    }
                }
            }
        }

        return ResponseData.okData(commentList);
    }
}
