package com.yuzhyn.hidoc.app.application.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.doc.*;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.mapper.doc.*;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysMachineStatusLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.service.DocAccessLogService;
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
                responseData.putDataMap("collected", collected);
                responseData.putDataMap("doc", doc);
                // 查询文章贡献者
                List<SysUserLite> contributorList = new ArrayList<>();
                // 查询文章贡献者：创建人
                SysUserLite createUser = sysUserLiteMapper.selectById(doc.getCreateUserId());
                contributorList.add(createUser);
                // 查询文章贡献者：拥有者
                SysUserLite ownerUser = sysUserLiteMapper.selectById(doc.getOwnerUserId());
                if (!createUser.getId().equals(ownerUser.getId())) contributorList.add(ownerUser);
                // 查询文章贡献者：团队成员
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

                // 统计信息查看
                // 总阅读量
                int readCount = docAccessLogMapper.selectCount(new LambdaQueryWrapper<DocAccessLog>().eq(DocAccessLog::getDocId, id));
                responseData.putDataMap("readCount", readCount);
                // 近7天阅读量
                List<Map> statisData = new ArrayList<>();
                LocalDate localDate = LocalDate.now();
                for (int i = 7; i >= 0; i--) {
                    Map map = new HashMap<>();
                    int count = docAccessLogMapper.selectCount(new LambdaQueryWrapper<DocAccessLog>()
                            .eq(DocAccessLog::getDocId, id).eq(DocAccessLog::getCreateDate, localDate.plusDays(i * -1)));
                    map.put("date", localDate.plusDays(i * -1L));
                    map.put("count", count);
                    statisData.add(map);
                }
                responseData.putDataMap("statisData", statisData);

                return responseData;
            }
        }
        return ResponseData.error();
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

                    // 判断编辑用户是否具备权限（管理、协作成员）
                    boolean isOwner = CurrentUserManager.getUser().getId().equals(doc.getOwnerUserId());
                    DocCollectedMember docCollectedMember = docCollectedMemberMapper.selectOne(new LambdaQueryWrapper<DocCollectedMember>()
                            .eq(DocCollectedMember::getCollectedId, doc.getCollectedId())
                            .eq(DocCollectedMember::getUserId, CurrentUserManager.getUser().getId()));
                    boolean isMember = docCollectedMember != null && StringTool.ok(docCollectedMember.getUserId());
                    if (isOwner || isMember) {

                        if (StringTool.ok(doc.getLockUserId()) && !CurrentUserManager.getUser().getId().equals(doc.getLockUserId())) {
                            // 已被其他用户锁定
                            responseData.setStatus("lock");
                            SysUserLite user = sysUserLiteMapper.selectById(doc.getLockUserId());
                            if (user != null) responseData.putDataMap("user", user);
                        } else {
                            // 未被锁定，锁定并且允许编辑
                            responseData.setStatus("ok");
                            DocLite docLite = docLiteMapper.selectById(id);
                            docLite.setLockUserId(CurrentUserManager.getUser().getId());
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
                        doc.setSerialNumber(Integer.MAX_VALUE); // 创建之初设置排序为最大值，排序到最后
                        doc.setCreateTime(LocalDateTime.now());
                        doc.setUpdateTime(LocalDateTime.now());
                        doc.setCreateUserId(CurrentUserManager.getUser().getId());
                        doc.setLockUserId("");
                        doc.setOwnerUserId(CurrentUserManager.getUser().getId());
                        doc.setIsDelete(false);
                        int flag = docMapper.insert(doc);
                        if (flag > 0) {
                            return ResponseData.ok();
                        }
                        break;
                    default:
                        Doc docRecord = docMapper.selectById(id);
                        if (docRecord != null) {
                            if (StringTool.ok(docRecord.getLockUserId()) && CurrentUserManager.getUser().getId().equals(docRecord.getLockUserId())) {
                                // 保存历史修改记录
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
                                docHistory.setCreateTime(LocalDateTime.now()); // 历史记录的创建时间是当前时间
                                docHistory.setCreateUserId(docRecord.getCreateUserId());
                                docHistory.setUpdateUserId(CurrentUserManager.getUser().getId());
                                int flagHistory = docHistoryMapper.insert(docHistory);
                                if (flagHistory > 0) {
                                    // 更新文档内容
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
                                    return ResponseData.error("文档历史记录保存失败");
                                }
                            } else {
                                return ResponseData.error("文档正在被他人编辑");
                            }
                        } else {
                            return ResponseData.error("未查询到文档");
                        }
                        break;
                }
            }
        }
        return ResponseData.error("创建失败，请填写完整信息");
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
                            return ResponseData.ok("文档解锁成功");
                        }
                    } else {
                        return ResponseData.error("他人锁定的文档不能解锁");
                    }
                } else {
                    return ResponseData.ok("文档未被锁定");
                }
            }
        }
        return ResponseData.error("解锁失败");
    }

    @PostMapping("delete")
    public ResponseData delete(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id")) {
            String id = MapTool.getString(params, "id", "");
            DocLite docLite = docLiteMapper.selectById(id);
            if (docLite != null) {
                String lockUserId = docLite.getLockUserId();
                if (StringTool.ok(lockUserId) && !lockUserId.equals(CurrentUserManager.getUser().getId())) {
                    return ResponseData.error("文档已被他人锁定");
                } else {
                    docLite.setUpdateTime(LocalDateTime.now());
                    docLite.setIsDelete(true);
                    int flag = docLiteMapper.updateById(docLite);
                    if (flag > 0) {
                        return ResponseData.ok();
                    }
                }
            }
        }
        return ResponseData.error("删除失败");
    }

    @PostMapping("restore")
    public ResponseData restore(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id")) {
            String id = MapTool.getString(params, "id", "");
            DocLite docLite = docLiteMapper.selectById(id);
            if (docLite != null) {
                docLite.setUpdateTime(LocalDateTime.now());
                docLite.setIsDelete(false);
                int flag = docLiteMapper.updateById(docLite);
                if (flag > 0) {
                    return ResponseData.ok();
                }
            }
        }
        return ResponseData.error("还原失败");
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
            responseData.putData(list);
        }
        return responseData;
    }

    @PostMapping("deletedList")
    public ResponseData deletedList(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        List<DocLite> list = docLiteMapper.selectList(new LambdaQueryWrapper<DocLite>()
                .eq(DocLite::getOwnerUserId, CurrentUserManager.getUser().getId())
                .eq(DocLite::getIsDelete, true)
                .orderByDesc(DocLite::getUpdateTime));
        if (ListTool.ok(list)) {
            for (DocLite doc : list) {
                SysUserLite ownerUser = sysUserLiteMapper.selectById(doc.getOwnerUserId());
                if (ownerUser != null) doc.setOwnerUser(ownerUser);

                DocCollected collected = docCollectedMapper.selectById(doc.getCollectedId());
                if (collected != null) doc.setCollectedName(collected.getName());
            }
        }
        responseData.putData(list);
        return responseData;
    }
}
