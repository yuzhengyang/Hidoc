package com.yuzhyn.hidoc.app.application.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.doc.DocCollected;
import com.yuzhyn.hidoc.app.application.entity.doc.DocCollectedMember;
import com.yuzhyn.hidoc.app.application.entity.doc.DocComment;
import com.yuzhyn.hidoc.app.application.entity.doc.DocLite;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocCollectedMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocCommentMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocLiteMapper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("doccomment")
public class DocCommentController {


    @Autowired
    DocCommentMapper docCommentMapper;

    @Autowired
    DocLiteMapper docLiteMapper;

    @Autowired
    SysUserLiteMapper sysUserLiteMapper;


    @PostMapping("create")
    public ResponseData create(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "docId", "content")) {
            String docId = MapTool.get(params, "docId", "").toString();
            String content = MapTool.get(params, "content", "").toString();
            String replyCommentId = MapTool.get(params, "replyCommentId", "").toString();
            String replyUserId = MapTool.get(params, "replyUserId", "").toString();

            if (StringTool.ok(docId, content)) {
                DocComment docComment = new DocComment();
                docComment.setId(R.SnowFlake.nexts());
                docComment.setDocId(docId);
                docComment.setCreateTime(LocalDateTime.now());
                docComment.setCreateUserId(CurrentUserManager.getUser().getId());
                docComment.setContent(content);
                docComment.setReplyCommentId(replyCommentId);
                docComment.setIsDelete(false);
                docComment.setIsDocOwnerRead(false);
                docComment.setDocOwnerReadTime(null);
                docComment.setReplyUserId(replyUserId);
                docComment.setIsReplyUserRead(false);
                docComment.setReplyUserReadTime(null);
                int flag = docCommentMapper.insert(docComment);
                if (flag > 0) {
                    return ResponseData.ok();
                } else {
                    return ResponseData.error("评论异常，请稍后重试");
                }
            }
        }
        return ResponseData.error("请输入内容");
    }


    @PostMapping("delete")
    public ResponseData delete(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id")) {
            String id = MapTool.get(params, "id", "").toString();
            DocComment docComment = docCommentMapper.selectById(id);
            if (docComment != null) {
                docComment.setIsDelete(true);
                int flag = docCommentMapper.updateById(docComment);
                if (flag > 0) return ResponseData.ok();
            } else {
                return ResponseData.error("未查询到相关评论");
            }
        }

        return ResponseData.error("请选择要删除的评论");
    }

    @PostMapping("read")
    public ResponseData read(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id", "type")) {
            String id = MapTool.get(params, "id", "").toString();
            String type = MapTool.get(params, "type", "").toString();

            String userId = CurrentUserManager.getUser().getId();
            DocComment docComment = docCommentMapper.selectById(id);
            if (docComment != null) {
                switch (type) {
                    case "owner": {
                        DocLite docLite = docLiteMapper.selectById(docComment.getDocId());
                        if (docLite != null) {
                            if (docLite.getOwnerUserId().equals(userId)) {
                                docComment.setIsDocOwnerRead(true);
                                docCommentMapper.updateById(docComment);
                            }
                        }
                        break;
                    }
                    case "reply": {
                        if (docComment.getReplyUserId().equals(userId)) {
                            docComment.setIsReplyUserRead(true);
                            docCommentMapper.updateById(docComment);
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
            return ResponseData.ok();
        }

        return ResponseData.error("请先选择评论");
    }

    @PostMapping("myList")
    public ResponseData myList(@RequestBody Map<String, Object> params) {
        // 获取参数信息
        String type = MapTool.get(params, "type", "").toString();

        // 配置查询条件
        LambdaQueryWrapper<DocComment> lambdaQueryWrapper = new LambdaQueryWrapper<DocComment>();
        if (type.equals("myCreated")) lambdaQueryWrapper = lambdaQueryWrapper.eq(DocComment::getCreateUserId, CurrentUserManager.getUserId());
        if (type.equals("replyMe")) lambdaQueryWrapper = lambdaQueryWrapper.eq(DocComment::getReplyUserId, CurrentUserManager.getUserId());

        lambdaQueryWrapper = lambdaQueryWrapper.eq(DocComment::getIsDelete, false).orderByDesc(DocComment::getCreateTime);
        List<DocComment> commentList = docCommentMapper.selectList(lambdaQueryWrapper);

        if (ListTool.ok(commentList)) {
            List<String> userIds = new ArrayList<>();
            List<String> userIds1 = commentList.stream().map(DocComment::getCreateUserId).distinct().collect(toList());
            List<String> userIds2 = commentList.stream().map(DocComment::getReplyUserId).distinct().collect(toList());
            userIds.addAll(userIds1);
            userIds.addAll(userIds2);
            List<SysUserLite> userLites = sysUserLiteMapper.selectBatchIds(userIds);

            List<String> docIds = commentList.stream().map(DocComment::getDocId).distinct().collect(toList());
            List<DocLite> docLites = docLiteMapper.selectBatchIds(docIds);

            for (DocComment commentItem : commentList) {
                // 填充用户信息
                if (ListTool.ok(userLites)) {
                    for (SysUserLite userItem : userLites) {
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
                // 填充文档信息
                if (ListTool.ok(docLites)) {
                    for (DocLite docItem : docLites) {
                        if(commentItem.getDocId().equals(docItem.getId())){
                            commentItem.setDocLite(docItem);
                        }
                    }
                }
            }
        }

        return ResponseData.okData(commentList);
    }
}
