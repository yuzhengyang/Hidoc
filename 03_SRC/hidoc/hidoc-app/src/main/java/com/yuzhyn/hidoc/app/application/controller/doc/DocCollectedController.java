package com.yuzhyn.hidoc.app.application.controller.doc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.doc.*;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.entity.team.TeamMember;
import com.yuzhyn.hidoc.app.application.mapper.doc.*;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMemberMapper;
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

    @Autowired
    TeamMemberMapper teamMemberMapper;

    @PostMapping("create")
    public ResponseData create(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "name", "token")) {
            String name = MapTool.get(params, "name", "").toString();
            String description = MapTool.get(params, "description", "").toString();
            Boolean isOpen = MapTool.getBoolean(params, "isOpen", false);
            Boolean isLoginAccess = MapTool.getBoolean(params, "isLoginAccess", false);
            Boolean isTemplet = MapTool.getBoolean(params, "isTemplet", false);
            String token = MapTool.get(params, "token", "").toString();
            Object teamIdListObject = MapTool.get(params, "teamIdList", null);

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
                if (teamIdListObject != null && teamIdListObject instanceof JSONArray)
                    docCollected.setTeamIdList((JSONArray) teamIdListObject);
                docCollected.setIsOpen(isOpen);
                docCollected.setIsLoginAccess(isLoginAccess);
                docCollected.setIsTemplet(isTemplet);
                docCollected.setIsDelete(false);
                int flag = docCollectedMapper.insert(docCollected);
                if (flag > 0) {
                    return ResponseData.okData("docCollected", docCollected);
                }
            }
        }
        return ResponseData.error("创建失败，请填写完整信息");
    }

    @PostMapping("edit")
    public ResponseData edit(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id", "name", "token")) {
            String id = MapTool.get(params, "id", "").toString();
            String name = MapTool.get(params, "name", "").toString();
            String description = MapTool.get(params, "description", "").toString();
            Boolean isOpen = MapTool.getBoolean(params, "isOpen", false);
            Boolean isLoginAccess = MapTool.getBoolean(params, "isLoginAccess", false);
            Boolean isTemplet = MapTool.getBoolean(params, "isTemplet", false);
            String token = MapTool.get(params, "token", "").toString();
            Object teamIdListObject = MapTool.get(params, "teamIdList", null);

            if (StringTool.ok(id, name, token)) {
                DocCollected record = docCollectedMapper.selectById(id);
                if (record != null) {
                    // 非所属者不能编辑
                    if (!record.getOwnerUserId().equals(CurrentUserManager.getUser().getId())) {
                        return ResponseData.error("编辑失败，非所属者不能编辑");
                    }

                    record.setName(name);
                    record.setDescription(description);
                    if (teamIdListObject != null && teamIdListObject instanceof JSONArray)
                        record.setTeamIdList((JSONArray) teamIdListObject);
                    record.setIsOpen(isOpen);
                    record.setIsLoginAccess(isLoginAccess);
                    record.setIsTemplet(isTemplet);
                    int flag = docCollectedMapper.updateById(record);
                    if (flag > 0) {
                        // 更新时，对文档数量为0的文集，进行补偿操作，更新文集中文档数量
                        if (record.getDocCount() == 0) docCollectedMapper.updateDocCount(id);
                        return ResponseData.okData("docCollected", record);
                    }
                }
            }
        }
        return ResponseData.error("编辑失败，请填写完整信息");
    }


    @PostMapping("delete")
    public ResponseData delete(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "id", "token")) {
            String id = MapTool.get(params, "id", "").toString();
            String token = MapTool.get(params, "token", "").toString();

            if (StringTool.ok(id, token)) {
                DocCollected record = docCollectedMapper.selectById(id);
                if (record != null) {
                    // 非所属者不能删除
                    if (!record.getOwnerUserId().equals(CurrentUserManager.getUser().getId())) {
                        return ResponseData.error("删除失败，非所属者不能删除");
                    }
                    // 存在文章不能删除
                    long existDocCount = docLiteMapper.selectCount(new LambdaQueryWrapper<DocLite>().eq(DocLite::getCollectedId, id).eq(DocLite::getIsDelete, false));
                    if (existDocCount > 0) {
                        return ResponseData.error("删除失败，存在文章不能删除");
                    }

                    record.setIsDelete(true);
                    int flag = docCollectedMapper.updateById(record);
                    if (flag > 0) {
                        return ResponseData.okData("docCollected", record);
                    }
                }
            }
        }
        return ResponseData.error("删除失败，请重新选择");
    }

    @PostMapping("list")
    public ResponseData list(@RequestBody Map<String, Object> params) {
        List<DocCollected> mineList = docCollectedMapper.selectList(new LambdaQueryWrapper<DocCollected>().eq(DocCollected::getOwnerUserId, CurrentUserManager.getUser().getId()).eq(DocCollected::getIsDelete, false));
        List<DocCollected> coopList = null;

        // 获取我加入的协作（不是属于我的的，也不是删除的）
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

        // 设置协作标志
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
        // 文集格式化名称，并排序
        docCollectedService.sortByFormatName(mineList);
        docCollectedService.sortByFormatName(coopList);

        responseData.putDataMap("mine", mineList);
        responseData.putDataMap("coop", coopList);

//        responseData.putDataMap("myCoop",null); // 我发起的协作
//        responseData.putDataMap("joinCoop",null); // 我加入的协作（别人创建的）
//        responseData.putDataMap("myOpen",null); // 我公开的文集
//        responseData.putDataMap("myPrivate",null); // 私有文集
//        responseData.putDataMap("myAll",mineList); // 我的全部

        return responseData;
    }

    @PostMapping("preview")
    public ResponseData preview(@RequestBody Map<String, Object> params) {
        List<DocCollected> collectedList = docCollectedService.search(params);
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

                // 查询文集中的文档列表
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

                // 查询文集成员
                List<SysUserLite> sysUserLites = new ArrayList<>();
                // 查询文集拥有者
                SysUserLite ownerUser = sysUserLiteMapper.selectById(collected.getOwnerUserId());
                ownerUser.setMemberDesc("管理文集");
                sysUserLites.add(ownerUser);
                // 查询文集创建者
//                SysUserLite createUser = sysUserLiteMapper.selectById(collected.getCreateUserId());
//                createUser.setMemberDesc("创建文集");
//                sysUserLites.add(createUser);
                // 查询文集包含的协作成员
                List<DocCollectedMember> docCollectedMembers = docCollectedMemberMapper.selectList(new LambdaQueryWrapper<DocCollectedMember>().eq(DocCollectedMember::getCollectedId, id));
                if (ListTool.ok(docCollectedMembers)) {
                    List<String> userIds = docCollectedMembers.stream().map(DocCollectedMember::getUserId).collect(toList());
                    List<SysUserLite> _userList = sysUserLiteMapper.selectBatchIds(userIds);
                    if (ListTool.ok(_userList)) {
                        for (SysUserLite item : _userList) {
                            if (!item.getId().equals(ownerUser.getId()) && !item.getIsFrozen()) {
                                item.setMemberDesc("协作成员");
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
                // 初始化顺序
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setSerialNumber(100 + ((i + 1) * 10));
                    if (list.get(i).getId().equals(docId)) curIndex = i;
                }
                // 对选定文章进行排序
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

                // 整理协作成员列表
                List<SysUserLite> memberUserList = null;
                List<String> memberIdList = null;
                if (ListTool.ok(memberList)) {
                    memberIdList = memberList.stream().map(DocCollectedMember::getUserId).collect(Collectors.toList());
                    memberUserList = sysUserLiteMapper.selectBatchIds(memberIdList);
                }
                // 整理未协作成员列表
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

                // 汇总结果并返回
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

            // 删除当前文集的协作人员记录
            docCollectedMemberMapper.delete(new LambdaQueryWrapper<DocCollectedMember>().eq(DocCollectedMember::getCollectedId, id));

            // 重新创建协作人员记录
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
        return ResponseData.error("保存协作成员失败，缺少参数");
    }


    /**
     * 获取当前用户对该文集文档的操作权限清单
     *
     * @param params
     * @return
     */
    @PostMapping("permission")
    public ResponseData permission(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        if (MapTool.ok(params, "id")) {
            String id = MapTool.getString(params, "id", "");

            // 是否所属人
            boolean isOwner = docCollectedMapper.exists(new LambdaQueryWrapper<DocCollected>()
                    .eq(DocCollected::getId, id)
                    .eq(DocCollected::getOwnerUserId, CurrentUserManager.getUserId()));
            // 是否协作成员
            boolean isMember = docCollectedMemberMapper.exists(new LambdaQueryWrapper<DocCollectedMember>()
                    .eq(DocCollectedMember::getCollectedId, id)
                    .eq(DocCollectedMember::getUserId, CurrentUserManager.getUserId())
                    .eq(DocCollectedMember::getAllowEdit, true));

            // 详情：只是用来查看的，登录就可以查看详情
            boolean detail = true;
            // 演示：登录就可以使用演示模式，方便演示或打印内容
            boolean focus = true;
            // 历史：文集所属人或协作成员，可以查看历史
            boolean his = isOwner || isMember;
            // 新建：文集所属人或协作成员，可以创建文档
            boolean create = isOwner || isMember;
            // 编辑：文集所属人或协作成员，可以编辑文档（由编辑界面提供文档是否正在编辑的提示）
            boolean edit = isOwner || isMember;
            // 复制：文集所属人或协作成员，可以复制文档（即创建新文档，携带当前文档文本内容）
            boolean copy = isOwner || isMember;
            // 协作：文集所属人或协作成员，可以查看并管理协作成员
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
