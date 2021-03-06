package com.yuzhyn.hidoc.app.application.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.doc.*;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.mapper.doc.*;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.model.sys.UserInfo;
import com.yuzhyn.hidoc.app.application.service.doc.DocCollectedService;
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
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.azylee.core.logs.Alog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("collected")
public class DocCollectedController {

    @Autowired
    DocCollectedMapper docCollectedMapper;

    @Autowired
    DocCollectedMemberMapper docCollectedMemberMapper;

    @Autowired
    DocLiteMapper docLiteMapper;

    @Autowired
    DocLiteOrderMapper docLiteOrderMapper;

    @Autowired
    DocMapper docMapper;

    @Autowired
    SysUserLiteMapper sysUserLiteMapper;

    @Autowired
    DocParseService docParseService;

    @Autowired
    DocCollectedService docCollectedService;

    @PostMapping("create")
    public ResponseData create(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "name", "token")) {
            String name = MapTool.get(params, "name", "").toString();
            String description = MapTool.get(params, "description", "").toString();
            Boolean isOpen = MapTool.getBoolean(params, "isOpen", false);
            Boolean isLoginAccess = MapTool.getBoolean(params, "isLoginAccess", false);
            String token = MapTool.get(params, "token", "").toString();

            if (StringTool.ok(name, token)) {
                DocCollected docCollected = new DocCollected();
                docCollected.setId(R.SnowFlake.nexts());
                docCollected.setCreateTime(LocalDateTime.now());
                UserInfo userInfo = R.Caches.UserInfo.get(token);
                Alog.i("Thread.currentThread: " + Thread.currentThread());
                docCollected.setCreateUserId(userInfo.getUser().getId());
                docCollected.setOwnerUserId(userInfo.getUser().getId());
                docCollected.setName(name);
                docCollected.setDescription(description);
                docCollected.setIsOpen(isOpen);
                docCollected.setIsLoginAccess(isLoginAccess);
                docCollected.setIsDelete(false);
                int flag = docCollectedMapper.insert(docCollected);
                if (flag > 0) {
                    return ResponseData.okData("docCollected", docCollected);
                }
            }
        }
        return ResponseData.error("????????????????????????????????????");
    }

    @PostMapping("edit")
    public ResponseData edit(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id", "name", "token")) {
            String id = MapTool.get(params, "id", "").toString();
            String name = MapTool.get(params, "name", "").toString();
            String description = MapTool.get(params, "description", "").toString();
            Boolean isOpen = MapTool.getBoolean(params, "isOpen", false);
            Boolean isLoginAccess = MapTool.getBoolean(params, "isLoginAccess", false);
            String token = MapTool.get(params, "token", "").toString();

            if (StringTool.ok(id, name, token)) {
                DocCollected record = docCollectedMapper.selectById(id);
                if (record != null) {
                    // ????????????????????????
                    if (!record.getOwnerUserId().equals(CurrentUserManager.getUser().getId())) {
                        return ResponseData.error("???????????????????????????????????????");
                    }

                    record.setName(name);
                    record.setDescription(description);
                    record.setIsOpen(isOpen);
                    record.setIsLoginAccess(isLoginAccess);
                    int flag = docCollectedMapper.updateById(record);
                    if (flag > 0) {
                        return ResponseData.okData("docCollected", record);
                    }
                }
            }
        }
        return ResponseData.error("????????????????????????????????????");
    }


    @PostMapping("delete")
    public ResponseData delete(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id", "token")) {
            String id = MapTool.get(params, "id", "").toString();
            String token = MapTool.get(params, "token", "").toString();

            if (StringTool.ok(id, token)) {
                DocCollected record = docCollectedMapper.selectById(id);
                if (record != null) {
                    // ????????????????????????
                    if (!record.getOwnerUserId().equals(CurrentUserManager.getUser().getId())) {
                        return ResponseData.error("???????????????????????????????????????");
                    }
                    // ????????????????????????
                    long existDocCount = docLiteMapper.selectCount(new LambdaQueryWrapper<DocLite>().eq(DocLite::getCollectedId, id).eq(DocLite::getIsDelete, false));
                    if (existDocCount > 0) {
                        return ResponseData.error("???????????????????????????????????????");
                    }

                    record.setIsDelete(true);
                    int flag = docCollectedMapper.updateById(record);
                    if (flag > 0) {
                        return ResponseData.okData("docCollected", record);
                    }
                }
            }
        }
        return ResponseData.error("??????????????????????????????");
    }

    @PostMapping("list")
    public ResponseData list(@RequestBody Map<String, Object> params) {
        List<DocCollected> mineList = docCollectedMapper.selectList(new LambdaQueryWrapper<DocCollected>().eq(DocCollected::getOwnerUserId, CurrentUserManager.getUser().getId()).eq(DocCollected::getIsDelete, false));
        List<DocCollected> coopList = null;

        // ????????????????????????????????????????????????????????????????????????
        List<DocCollectedMember> memberList = docCollectedMemberMapper.selectList(new LambdaQueryWrapper<DocCollectedMember>().eq(DocCollectedMember::getUserId, CurrentUserManager.getUser().getId()));
        if (ListTool.ok(memberList)) {
            List<String> idList = memberList.stream().map(DocCollectedMember::getCollectedId).collect(Collectors.toList());
            if (ListTool.ok(idList)) {
                coopList = docCollectedMapper.selectBatchIds(idList);
                if (ListTool.ok(coopList)) {
                    for (int i = coopList.size() - 1; i >= 0; i--) {
                        if (coopList.get(i).getOwnerUserId().equals(CurrentUserManager.getUserId()) || coopList.get(i).getIsDelete()) {
                            coopList.remove(i);
                        }
                    }
                }
            }
        }

        // ??????????????????
        if (ListTool.ok(mineList)) {
            for (DocCollected item : mineList) {
                long count = docCollectedMemberMapper.selectCount(new LambdaQueryWrapper<DocCollectedMember>()
                        .eq(DocCollectedMember::getCollectedId, item.getId())
                        .ne(DocCollectedMember::getUserId, item.getOwnerUserId()));
                if (count > 0) {
                    item.setIsCoop(true);
                }
            }
        }

        ResponseData responseData = ResponseData.ok();
        // ?????????????????????????????????
        docCollectedService.sortByFormatName(mineList);
        docCollectedService.sortByFormatName(coopList);

        responseData.putDataMap("mine", mineList);
        responseData.putDataMap("coop", coopList);

//        responseData.putDataMap("myCoop",null); // ??????????????????
//        responseData.putDataMap("joinCoop",null); // ???????????????????????????????????????
//        responseData.putDataMap("myOpen",null); // ??????????????????
//        responseData.putDataMap("myPrivate",null); // ????????????
//        responseData.putDataMap("myAll",mineList); // ????????????

        return responseData;
    }

    @PostMapping("preview")
    public ResponseData preview(@RequestBody Map<String, Object> params) {
        // ?????????????????????
        int pageSize = 5;
        String keyword = MapTool.get(params, "keyword", "").toString();
        String from = MapTool.get(params, "from", "").toString();

        String[] keywordArray = StringTool.split(keyword, " ", true, true);
        List<String> collectedIds = new ArrayList<>();
        List<String> docIds = new ArrayList<>();

        // ?????????????????????????????????
        if (StringTool.ok(keyword)) {
            collectedIds.add("*not_exist");
            docIds.add("*not_exist");
            pageSize = 20;

            if (from.equals("editor")) pageSize = 100;
            // ???????????????????????????
            {
                LambdaQueryWrapper<DocLite> preDocWrapper = new LambdaQueryWrapper<DocLite>();
                preDocWrapper = preDocWrapper.and(q -> q.eq(DocLite::getIsDelete, false));
                if (ListTool.ok(keywordArray)) {
                    preDocWrapper = preDocWrapper.and(p -> {
                        for (String key : keywordArray) {
                            String keyLike = "%" + key + "%";
                            p.apply("(COALESCE(title,'') ILIKE {0} OR COALESCE(content,'') ILIKE {0})", keyLike);
                        }
                    });
                    List<DocLite> preDocList = docLiteMapper.selectList(preDocWrapper);
                    if (ListTool.ok(preDocList)) {
                        // ??????????????????id??????
                        docIds.addAll(preDocList.stream().map(DocLite::getId).distinct().collect(toList()));
                        // ??????????????????id??????
                        collectedIds.addAll(preDocList.stream().map(DocLite::getCollectedId).distinct().collect(toList()));
                    }
                }
            }
        }

        // ?????????????????????????????????????????????????????????
        LambdaQueryWrapper<DocCollected> docCollectedLambdaQueryWrapper = new LambdaQueryWrapper<DocCollected>().eq(DocCollected::getIsOpen, true).eq(DocCollected::getIsDelete, false);
        if (ListTool.ok(collectedIds))
            docCollectedLambdaQueryWrapper = docCollectedLambdaQueryWrapper.in(DocCollected::getId, collectedIds);

        List<DocCollected> collectedList = docCollectedMapper.selectList(docCollectedLambdaQueryWrapper);
        if (ListTool.ok(collectedList)) {
            for (DocCollected collected : collectedList) {
                Page<DocLite> page = new Page<>(1, pageSize);
                // ?????????????????????????????????????????????????????????
                LambdaQueryWrapper<DocLite> docLiteLambdaQueryWrapper = new LambdaQueryWrapper<DocLite>().eq(DocLite::getCollectedId, collected.getId()).eq(DocLite::getIsDelete, false).orderByDesc(DocLite::getUpdateTime);
                if (ListTool.ok(docIds))
                    docLiteLambdaQueryWrapper = docLiteLambdaQueryWrapper.in(DocLite::getId, docIds);

                IPage<DocLite> docLites = docLiteMapper.selectPage(page, docLiteLambdaQueryWrapper);
                if (docLites.getTotal() > 0) {
                    collected.setDocLites(docLites.getRecords());
                    collected.setDocTotal(docLites.getTotal());
                }
            }
            for (int i = collectedList.size() - 1; i >= 0; i--) {
                if (!ListTool.ok(collectedList.get(i).getDocLites())) {
                    collectedList.remove(i);
                }
            }
            // ?????????????????????????????????
            docCollectedService.sortByFormatName(collectedList);
        }
        return ResponseData.okData(collectedList);
    }

    @PostMapping("get")
    public ResponseData get(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id")) {
            String id = MapTool.get(params, "id", "").toString();
            String keyword = MapTool.get(params, "keyword", "").toString();
            String keywordLike = "%" + keyword.replace(' ', '%') + "%";
            DocCollected collected = docCollectedMapper.selectById(id);
            if (collected != null) {

                // ??????????????????????????????
                LambdaQueryWrapper<DocLite> wrapper = new LambdaQueryWrapper<DocLite>()
                        .eq(DocLite::getCollectedId, id)
                        .eq(DocLite::getIsDelete, false)
                        .orderByAsc(DocLite::getSerialNumber, DocLite::getCreateTime);
                if (StringTool.ok(keyword)) {
                    List<Doc> likeList = docMapper.selectList(new LambdaQueryWrapper<Doc>()
                            .eq(Doc::getCollectedId, id)
                            .eq(Doc::getIsDelete, false)
                            .and(i -> i.like(Doc::getTitle, keywordLike).or().like(Doc::getContent, keywordLike)));

                    if (ListTool.ok(likeList)) {
                        List<String> ids = likeList.stream().map(Doc::getId).collect(toList());
                        wrapper.in(DocLite::getId, ids);
                    } else {
                        wrapper.isNull(DocLite::getId);
                    }
                }
                List<DocLite> docLiteList = docLiteMapper.selectList(wrapper);
                collected.setDocLites(docParseService.docLite2Tree(docLiteList));

                // ??????????????????
                List<SysUserLite> sysUserLites = new ArrayList<>();
                // ?????????????????????
                SysUserLite ownerUser = sysUserLiteMapper.selectById(collected.getOwnerUserId());
                ownerUser.setMemberDesc("????????????");
                sysUserLites.add(ownerUser);
                // ?????????????????????
                SysUserLite createUser = sysUserLiteMapper.selectById(collected.getCreateUserId());
                createUser.setMemberDesc("????????????");
                sysUserLites.add(createUser);
                // ?????????????????????????????????
                List<DocCollectedMember> docCollectedMembers = docCollectedMemberMapper.selectList(new LambdaQueryWrapper<DocCollectedMember>().eq(DocCollectedMember::getCollectedId, id));
                if (ListTool.ok(docCollectedMembers)) {
                    List<String> userIds = docCollectedMembers.stream().map(DocCollectedMember::getUserId).collect(toList());
                    List<SysUserLite> _userList = sysUserLiteMapper.selectBatchIds(userIds);
                    if (ListTool.ok(_userList)) {
                        for (SysUserLite item : _userList) {
                            if (!item.getId().equals(ownerUser.getId())) {
                                item.setMemberDesc("????????????");
                                sysUserLites.add(item);
                            }
                        }
                    }
                }
                collected.setSysUserLites(sysUserLites);

                ResponseData responseData = ResponseData.ok();
                responseData.putDataMap("collected", collected);
                return responseData;
            }
        }
        return ResponseData.error();
    }

    @Transactional
    @PostMapping("setOrder")
    public ResponseData setOrder(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "collectedId", "docId", "vector")) {
            String collectedId = MapTool.get(params, "collectedId", "").toString();
            String docId = MapTool.get(params, "docId", "").toString();
            int vector = MapTool.getInt(params, "vector", 99);

            List<DocLiteOrder> list = docLiteOrderMapper.selectList(new LambdaQueryWrapper<DocLiteOrder>()
                    .eq(DocLiteOrder::getCollectedId, collectedId)
                    .eq(DocLiteOrder::getIsDelete, false)
                    .orderByAsc(DocLiteOrder::getSerialNumber, DocLiteOrder::getCreateTime));
            if (ListTool.ok(list)) {
                int curIndex = -1;
                // ???????????????
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setSerialNumber(100 + ((i + 1) * 10));
                    if (list.get(i).getId().equals(docId)) curIndex = i;
                }
                // ???????????????????????????
                if (curIndex >= 0) {
                    switch (vector) {
                        case 0:
                            list.get(curIndex).setSerialNumber(10);
                            break;
                        case 1:
                            list.get(curIndex).setSerialNumber(list.get(curIndex).getSerialNumber() - 15);
                            break;
                        case 2:
                            list.get(curIndex).setSerialNumber(list.get(curIndex).getSerialNumber() + 15);
                            break;
                        case 3:
                            list.get(curIndex).setSerialNumber(100 + ((list.size() + 1) * 10));
                            break;
                        default:
                            break;
                    }
                }
                for (DocLiteOrder item : list) {
                    docLiteOrderMapper.updateById(item);
                }
            }
        }
        return ResponseData.ok();
    }

    @Transactional
    @PostMapping("freeOrder")
    public ResponseData freeOrder(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "docList")) {
            Object _docList = MapTool.get(params, "docList", null);
            if (_docList != null) {
                JSONArray docList = (JSONArray) _docList;

                for (int i = 0; i < docList.size(); i++) {
                    JSONObject doc = docList.getJSONObject(i);
                    String id = doc.getString("id");
                    String pid = doc.getString("pid");
                    int level = doc.getInteger("level");

                    DocLiteOrder docLiteOrder = docLiteOrderMapper.selectById(id);
                    docLiteOrder.setParentDocId(pid);
                    docLiteOrder.setSerialNumber(i);
                    docLiteOrderMapper.updateById(docLiteOrder);

                }
            }
        }
        return ResponseData.ok();
    }


    @PostMapping("getMember")
    public ResponseData getMember(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id")) {
            String id = MapTool.get(params, "id", "").toString();
            DocCollected collected = docCollectedMapper.selectById(id);
            if (collected != null) {
                List<DocCollectedMember> memberList = docCollectedMemberMapper.selectList(new LambdaQueryWrapper<DocCollectedMember>().eq(DocCollectedMember::getCollectedId, id));

                // ????????????????????????
                List<SysUserLite> memberUserList = null;
                List<String> memberIdList = null;
                if (ListTool.ok(memberList)) {
                    memberIdList = memberList.stream().map(DocCollectedMember::getUserId).collect(Collectors.toList());
                    memberUserList = sysUserLiteMapper.selectBatchIds(memberIdList);
                }
                // ???????????????????????????
                List<SysUserLite> otherUserList = null;
                List<String> otherUserIdList = null;
                List<SysUserLite> allUserList = sysUserLiteMapper.selectList(new LambdaQueryWrapper<SysUserLite>().eq(SysUserLite::getIsFrozen, false));
                if (ListTool.ok(allUserList)) {
                    otherUserIdList = allUserList.stream().map(SysUserLite::getId).collect(Collectors.toList());
                    if (ListTool.ok(otherUserIdList) && ListTool.ok(memberIdList))
                        otherUserIdList.removeAll(memberIdList);
                    if (ListTool.ok(otherUserIdList))
                        otherUserList = sysUserLiteMapper.selectBatchIds(otherUserIdList);
                }

                // ?????????????????????
                ResponseData responseData = ResponseData.ok();
                responseData.putDataMap("collected", collected);
                responseData.putDataMap("memberUser", memberUserList);
                responseData.putDataMap("memberId", memberIdList);
                responseData.putDataMap("otherUser", otherUserList);
                responseData.putDataMap("allUser", allUserList);
                return responseData;
            }
        }
        return ResponseData.error();
    }


    @Transactional
    @PostMapping("saveMember")
    public ResponseData saveMember(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id", "memberId")) {
            String id = MapTool.get(params, "id", "").toString();
            List<String> memberId = (List<String>) MapTool.get(params, "memberId", null);

            // ???????????????????????????????????????
            docCollectedMemberMapper.delete(new LambdaQueryWrapper<DocCollectedMember>().eq(DocCollectedMember::getCollectedId, id));

            // ??????????????????????????????
            if (ListTool.ok(memberId)) {
                for (String userId : memberId) {
                    DocCollectedMember member = new DocCollectedMember();
                    member.setId(R.SnowFlake.nexts());
                    member.setCreateTime(LocalDateTime.now());
                    member.setCollectedId(id);
                    member.setAllowEdit(true);
                    member.setUserId(userId);
                    member.setCreateUserId(CurrentUserManager.getUser().getId());
                    docCollectedMemberMapper.insert(member);
                }
            }
            return ResponseData.ok();
        }
        return ResponseData.error("???????????????????????????????????????");
    }


    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param params
     * @return
     */
    @PostMapping("permission")
    public ResponseData permission(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        if (MapTool.ok(params, "id")) {
            String id = MapTool.getString(params, "id", "");

            // ???????????????
            boolean isOwner = docCollectedMapper.exists(new LambdaQueryWrapper<DocCollected>()
                    .eq(DocCollected::getId, id)
                    .eq(DocCollected::getOwnerUserId, CurrentUserManager.getUserId()));
            // ??????????????????
            boolean isMember = docCollectedMemberMapper.exists(new LambdaQueryWrapper<DocCollectedMember>()
                    .eq(DocCollectedMember::getCollectedId, id)
                    .eq(DocCollectedMember::getUserId, CurrentUserManager.getUserId())
                    .eq(DocCollectedMember::getAllowEdit, true));

            // ????????????????????????????????????????????????????????????
            boolean detail = true;
            // ????????????????????????????????????????????????????????????????????????
            boolean focus = true;
            // ????????????????????????????????????????????????????????????
            boolean his = isOwner || isMember;
            // ????????????????????????????????????????????????????????????
            boolean create = isOwner || isMember;
            // ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            boolean edit = isOwner || isMember;
            // ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            boolean copy = isOwner || isMember;
            // ???????????????????????????????????????????????????????????????????????????
            boolean member = isOwner || isMember;

            responseData.putDataMap("detail", detail);
            responseData.putDataMap("focus", focus);
            responseData.putDataMap("his", his);
            responseData.putDataMap("create", create);
            responseData.putDataMap("edit", edit);
            responseData.putDataMap("copy", copy);
            responseData.putDataMap("member", member);
        }
        return responseData;
    }


}
