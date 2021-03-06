package com.yuzhyn.hidoc.app.application.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.doc.*;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLiteOnline;
import com.yuzhyn.hidoc.app.application.mapper.doc.*;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysMachineStatusLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.service.DocAccessLogService;
import com.yuzhyn.hidoc.app.application.service.doc.DocParseService;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.datetimes.RelativeDateFormat;
import com.yuzhyn.azylee.core.datas.strings.StringTool;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("doc")
public class DocController {

    @Autowired
    DocCollectedMapper docCollectedMapper;

    @Autowired
    DocCollectedMemberMapper docCollectedMemberMapper;

    @Autowired
    DocLiteMapper docLiteMapper;

    @Autowired
    SysUserLiteMapper sysUserLiteMapper;

    @Autowired
    SysMachineStatusLogMapper sysMachineStatusLogMapper;

    @Autowired
    DocMapper docMapper;

    @Autowired
    DocHistoryMapper docHistoryMapper;

    @Autowired
    DocHistoryLiteMapper docHistoryLiteMapper;

    @Autowired
    DocAccessLogService docAccessLogService;

    @Autowired
    DocAccessLogMapper docAccessLogMapper;

    @Autowired
    DocParseService docParseService;

    @Autowired
    DocThumbMapper docThumbMapper;

    @Autowired
    DocCommentMapper docCommentMapper;

    @PostMapping("get")
    public ResponseData get(@RequestBody Map<String, Object> params) {
        R.AccessTimes++;
        R.TodayAccessTimes++;

        if (MapTool.ok(params, "id")) {
            String id = MapTool.get(params, "id", "").toString();
            Doc doc = docMapper.selectById(id);
            if (doc != null) {
                docAccessLogService.createLog(doc);

                ResponseData responseData = ResponseData.ok();
                DocCollected collected = docCollectedMapper.selectById(doc.getCollectedId());

                // ???????????????????????????
                if (collected != null) {
                    // ??????????????????????????????????????????????????????????????????????????????????????????
                    // ??????????????????????????????????????????????????????
                    if (collected.getIsLoginAccess() != null && collected.getIsLoginAccess() && !CurrentUserManager.isLogin()) {
                        return ResponseData.error("?????????????????????????????????");
                    }

                    // ????????????????????????????????????????????????????????????????????????????????????????????????
                    // ??????????????????????????????????????????????????????
                    if (collected.getIsOpen() != null && !collected.getIsOpen()) {
                        if (!collected.getOwnerUserId().equals(CurrentUserManager.getUser().getId())) {
                            return ResponseData.error("????????????????????????");
                        }
                    }
                }

                responseData.putDataMap("collected", collected);
                responseData.putDataMap("doc", doc);
                // ?????????????????????
                List<SysUserLite> contributorList = new ArrayList<>();
                // ?????????????????????????????????
                SysUserLite createUser = sysUserLiteMapper.selectById(doc.getCreateUserId());
                contributorList.add(createUser);
                // ?????????????????????????????????
                SysUserLite ownerUser = sysUserLiteMapper.selectById(doc.getOwnerUserId());
                if (!createUser.getId().equals(ownerUser.getId())) contributorList.add(ownerUser);
                // ????????????????????????????????????
                List<DocHistoryLite> docHistoryLites = docHistoryLiteMapper.selectList(new LambdaQueryWrapper<DocHistoryLite>().eq(DocHistoryLite::getDocId, id));
                if (ListTool.ok(docHistoryLites)) {
                    List<String> ids = docHistoryLites.stream().map(DocHistoryLite::getUpdateUserId).distinct().collect(toList());
                    List<SysUserLite> _userLites = sysUserLiteMapper.selectBatchIds(ids);
                    if (ListTool.ok(_userLites)) {
                        for (SysUserLite item : _userLites) {
                            if (!item.getId().equals(createUser.getId())) {
                                contributorList.add(item);
                            }
                        }
                    }
                }
                responseData.putDataMap("contributors", contributorList);

                if (CurrentUserManager.isLogin()) {
                    if (StringTool.ok(doc.getLockUserId()) && !CurrentUserManager.getUser().getId().equals(doc.getLockUserId())) {
                        SysUserLite user = sysUserLiteMapper.selectById(doc.getLockUserId());
                        if (user != null) responseData.putDataMap("user", user);
                    }
                }

                // ??????????????????
                // ????????????
                long readCount = docAccessLogMapper.selectCount(new LambdaQueryWrapper<DocAccessLog>().eq(DocAccessLog::getDocId, id));
                responseData.putDataMap("readCount", readCount);
                // ???7????????????
                List<Map> statisData = new ArrayList<>();
                LocalDate localDate = LocalDate.now();
                for (int i = 7; i >= 0; i--) {
                    Map map = new HashMap<>();
                    long count = docAccessLogMapper.selectCount(new LambdaQueryWrapper<DocAccessLog>()
                            .eq(DocAccessLog::getDocId, id).eq(DocAccessLog::getCreateDate, localDate.plusDays(i * -1)));
                    map.put("date", localDate.plusDays(i * -1L));
                    map.put("count", count);
                    statisData.add(map);
                }
                responseData.putDataMap("statisData", statisData);

                // ??????????????????
                long thumbCount = docThumbMapper.selectCount(new LambdaQueryWrapper<DocThumb>()
                        .eq(DocThumb::getTableName, "doc")
                        .eq(DocThumb::getDataId, id)
                        .eq(DocThumb::getIsSupporter, true));
                responseData.putDataMap("thumbCount", thumbCount);

                if (CurrentUserManager.isLogin()) {
                    String thumbsId = DocThumb.genId("doc", id, CurrentUserManager.getUser().getId());
                    DocThumb myThumb = docThumbMapper.selectById(thumbsId);
                    if (myThumb == null) myThumb = new DocThumb();
                    responseData.putDataMap("myThumb", myThumb);
                }
                // ??????????????????
                long commentCount = docCommentMapper.selectCount(new LambdaQueryWrapper<DocComment>()
                        .eq(DocComment::getIsDelete, false)
                        .eq(DocComment::getDocId, id));
                responseData.putDataMap("commentCount", commentCount);

                // ????????????????????????
                List<String> replyUserIds = new ArrayList<>();
                if (CurrentUserManager.isLogin()) replyUserIds.add(CurrentUserManager.getUser().getId());
                if (ListTool.ok(contributorList)) {
                    replyUserIds.addAll(contributorList.stream().map(SysUserLite::getId).distinct().collect(toList()));
                }
                if (commentCount > 0) {
                    List<DocComment> docCommentList = docCommentMapper.selectList(new LambdaQueryWrapper<DocComment>()
                            .eq(DocComment::getIsDelete, false)
                            .eq(DocComment::getDocId, id));
                    if (ListTool.ok(docCommentList)) {
                        replyUserIds.addAll(docCommentList.stream().map(DocComment::getCreateUserId).distinct().collect(toList()));
                    }
                }
                List<SysUserLite> replyUserList = sysUserLiteMapper.selectBatchIds(replyUserIds);
                responseData.putDataMap("replyUserList", replyUserList);

                return responseData;
            }
        }
        return ResponseData.error();
    }

    @PostMapping("getPath")
    public ResponseData getPath(@RequestBody Map<String, Object> params) {
        String path = "";
        if (MapTool.ok(params, "parentDocId")) {
            String parentDocId = MapTool.get(params, "parentDocId", "").toString();
            if (StringTool.ok(parentDocId)) {
                DocLite pDoc = docLiteMapper.selectById(parentDocId);
                // ?????????????????????????????????????????????????????????10???
                for (int i = 0; i < 10; i++) {
                    if (pDoc != null) {
                        path = pDoc.getTitle() + "/" + path;
                        pDoc = docLiteMapper.selectById(pDoc.getParentDocId());
                    }
                    if (pDoc == null) break;
                }
            }
        }
        return ResponseData.okData("path", path);
    }

    @PostMapping("getForEdit")
    public ResponseData getForEdit(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id")) {
            String id = MapTool.get(params, "id", "").toString();
            Doc doc = docMapper.selectById(id);
            if (doc != null) {
                ResponseData responseData = ResponseData.ok();
                DocCollected collected = docCollectedMapper.selectById(doc.getCollectedId());
                doc.setIsCurrentUserLock(false);
                if (CurrentUserManager.isLogin()) {

                    // ???????????????????????????????????????????????????????????????
                    boolean isOwner = CurrentUserManager.getUser().getId().equals(doc.getOwnerUserId());
                    DocCollectedMember docCollectedMember = docCollectedMemberMapper.selectOne(new LambdaQueryWrapper<DocCollectedMember>()
                            .eq(DocCollectedMember::getCollectedId, doc.getCollectedId())
                            .eq(DocCollectedMember::getUserId, CurrentUserManager.getUser().getId()));
                    boolean isMember = docCollectedMember != null && StringTool.ok(docCollectedMember.getUserId());
                    if (isOwner || isMember) {

                        if (StringTool.ok(doc.getLockUserId()) && !CurrentUserManager.getUser().getId().equals(doc.getLockUserId())) {
                            // ????????????????????????
                            responseData.setStatus("lock");
                            SysUserLite user = sysUserLiteMapper.selectById(doc.getLockUserId());
                            if (user != null) responseData.putDataMap("user", user);
                        } else {
                            // ???????????????????????????????????????
                            responseData.setStatus("ok");
                            DocLite docLite = docLiteMapper.selectById(id);
                            docLite.setLockUserId(CurrentUserManager.getUser().getId());
                            docLite.setLockTime(LocalDateTime.now());
                            int flag = docLiteMapper.updateById(docLite);
                            if (flag > 0) doc.setIsCurrentUserLock(true);
                        }
                    } else {
                        responseData.setStatus("unauth");
                    }
                }
                responseData.putDataMap("collected", collected);
                responseData.putDataMap("doc", doc);
                return responseData;
            }
        }
        return ResponseData.error();
    }

    @Transactional
    @PostMapping("save")
    public ResponseData save(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "title", "content", "tag", "collectedId", "mode")) {
            String title = MapTool.get(params, "title", "").toString();
            String content = MapTool.get(params, "content", "").toString();
            String tag = MapTool.get(params, "tag", "").toString();
            String collectedId = MapTool.getString(params, "collectedId", "");
            String parentDocId = MapTool.getString(params, "parentDocId", "");
            String mode = MapTool.getString(params, "mode", "");
            String id = MapTool.getString(params, "id", "");

            if (StringTool.ok(title, content, tag)) {
                switch (mode) {
                    case "create":
                        Doc doc = new Doc();
                        doc.setId(R.SnowFlake.nexts());
                        doc.setCollectedId(collectedId);
                        doc.setDocType("");
                        doc.setTitle(title);
                        doc.setContent(content);
                        doc.setContentLength(content.length());
                        doc.setContentType("");
                        doc.setTag(tag);
                        doc.setSerialNumber(Integer.MAX_VALUE); // ??????????????????????????????????????????????????????
                        doc.setCreateTime(LocalDateTime.now());
                        doc.setUpdateTime(LocalDateTime.now());
                        doc.setCreateUserId(CurrentUserManager.getUser().getId());
                        doc.setLockUserId("");
                        doc.setOwnerUserId(CurrentUserManager.getUser().getId());
                        doc.setIsDelete(false);
                        doc.setParentDocId(parentDocId);
                        int flag = docMapper.insert(doc);
                        if (flag > 0) {
                            return ResponseData.ok();
                        }
                        break;
                    default:
                        Doc docRecord = docMapper.selectById(id);
                        if (docRecord != null) {
                            if (StringTool.ok(docRecord.getLockUserId()) && CurrentUserManager.getUser().getId().equals(docRecord.getLockUserId())) {
                                // ????????????????????????
                                DocHistory docHistory = new DocHistory();
                                docHistory.setId(R.SnowFlake.nexts());
                                docHistory.setDocId(docRecord.getId());
                                docHistory.setCollectedId(docRecord.getCollectedId());
                                docHistory.setDocType(docRecord.getDocType());
                                docHistory.setTitle(docRecord.getTitle());
                                docHistory.setContent(docRecord.getContent());
                                docHistory.setContentLength(docRecord.getContentLength());
                                docHistory.setContentType(docRecord.getContentType());
                                docHistory.setTag(docRecord.getTag());
                                docHistory.setSerialNumber(docRecord.getSerialNumber());
                                docHistory.setCreateTime(LocalDateTime.now()); // ??????????????????????????????????????????
                                docHistory.setCreateUserId(docRecord.getCreateUserId());
                                docHistory.setUpdateUserId(CurrentUserManager.getUser().getId());
                                int flagHistory = docHistoryMapper.insert(docHistory);
                                if (flagHistory > 0) {
                                    // ??????????????????
                                    docRecord.setId(id);
                                    docRecord.setCollectedId(collectedId);
                                    docRecord.setDocType("");
                                    docRecord.setTitle(title);
                                    docRecord.setContent(content);
                                    docRecord.setContentLength(content.length());
                                    docRecord.setContentType("");
                                    docRecord.setTag(tag);
                                    docRecord.setUpdateTime(LocalDateTime.now());
                                    docRecord.setUpdateUserId(CurrentUserManager.getUser().getId());
                                    docRecord.setLockUserId("");
                                    int flag2 = docMapper.updateById(docRecord);
                                    if (flag2 > 0) {
                                        return ResponseData.ok();
                                    }
                                } else {
                                    return ResponseData.error("??????????????????????????????");
                                }
                            } else {
                                return ResponseData.error("???????????????????????????");
                            }
                        } else {
                            return ResponseData.error("??????????????????");
                        }
                        break;
                }
            }
        }
        return ResponseData.error("????????????????????????????????????");
    }

    @PostMapping("unlock")
    public ResponseData unlock(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id")) {
            String id = MapTool.getString(params, "id", "");
            DocLite docLite = docLiteMapper.selectById(id);
            if (docLite != null) {
                String lockUserId = docLite.getLockUserId();
                if (lockUserId != null && !lockUserId.equals("")) {
                    if (lockUserId.equals(CurrentUserManager.getUser().getId())) {
                        docLite.setLockUserId("");
                        int flag = docLiteMapper.updateById(docLite);
                        if (flag > 0) {
                            return ResponseData.ok("??????????????????");
                        }
                    } else {
                        return ResponseData.error("?????????????????????????????????");
                    }
                } else {
                    return ResponseData.ok("??????????????????");
                }
            }
        }
        return ResponseData.error("????????????");
    }

    @PostMapping("delete")
    public ResponseData delete(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id")) {
            String id = MapTool.getString(params, "id", "");
            DocLite docLite = docLiteMapper.selectById(id);
            if (docLite != null) {
                String lockUserId = docLite.getLockUserId();
                if (StringTool.ok(lockUserId) && !lockUserId.equals(CurrentUserManager.getUser().getId())) {
                    return ResponseData.error("????????????????????????");
                } else {
//                    docLite.setUpdateTime(LocalDateTime.now());
                    docLite.setDeleteTime(LocalDateTime.now());
                    docLite.setDeleteUserId(CurrentUserManager.getUser().getId());
                    docLite.setIsDelete(true);
                    int flag = docLiteMapper.updateById(docLite);
                    if (flag > 0) {
                        return ResponseData.ok();
                    }
                }
            }
        }
        return ResponseData.error("????????????");
    }

    @PostMapping("restore")
    public ResponseData restore(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id")) {
            String id = MapTool.getString(params, "id", "");
            DocLite docLite = docLiteMapper.selectById(id);
            if (docLite != null) {
//                docLite.setUpdateTime(LocalDateTime.now());
                docLite.setIsDelete(false);
                int flag = docLiteMapper.updateById(docLite);
                if (flag > 0) {
                    return ResponseData.ok();
                }
            }
        }
        return ResponseData.error("????????????");
    }

    @PostMapping("list")
    public ResponseData list(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        if (MapTool.ok(params, "collectedId")) {
            String collectedId = MapTool.getString(params, "collectedId", "");
            List<DocLite> list = docLiteMapper.selectList(new LambdaQueryWrapper<DocLite>()
                    .eq(DocLite::getCollectedId, collectedId)
                    .eq(DocLite::getIsDelete, false)
                    .orderByAsc(DocLite::getSerialNumber, DocLite::getCreateTime));
            for (DocLite doc : list) {
                SysUserLite ownerUser = sysUserLiteMapper.selectById(doc.getOwnerUserId());
                if (ownerUser != null) doc.setOwnerUser(ownerUser);

                SysUserLite lockUser = sysUserLiteMapper.selectById(doc.getLockUserId());
                if (lockUser != null) {
                    doc.setLockUser(lockUser);
                    if (lockUser.getId().equals(CurrentUserManager.getUser().getId()))
                        doc.setIsCurrentUserLock(true);
                }
            }
            responseData.putData(docParseService.docLite2Tree(list));
        }
        return responseData;
    }

    @PostMapping("deletedList")
    public ResponseData deletedList(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        LocalDateTime expireTime = LocalDateTime.now().minusDays(180);
        List<DocLite> list = docLiteMapper.selectList(new LambdaQueryWrapper<DocLite>()
                .eq(DocLite::getOwnerUserId, CurrentUserManager.getUser().getId())
                .eq(DocLite::getIsDelete, true)
                .ge(DocLite::getDeleteTime, expireTime)
                .orderByDesc(DocLite::getDeleteTime));
        if (ListTool.ok(list)) {
            for (DocLite doc : list) {
                SysUserLite ownerUser = sysUserLiteMapper.selectById(doc.getOwnerUserId());
                if (ownerUser != null) doc.setOwnerUser(ownerUser);

                if (StringTool.ok(doc.getDeleteUserId())) {
                    SysUserLite deleteUser = sysUserLiteMapper.selectById(doc.getDeleteUserId());
                    if (deleteUser != null) doc.setDeleteUser(deleteUser);
                }

                DocCollected collected = docCollectedMapper.selectById(doc.getCollectedId());
                if (collected != null) doc.setCollectedName(collected.getName());
            }
        }
        responseData.putData(list);
        return responseData;
    }
}
