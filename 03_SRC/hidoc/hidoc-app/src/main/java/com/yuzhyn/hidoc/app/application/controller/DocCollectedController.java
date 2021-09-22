package com.yuzhyn.hidoc.app.application.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.sys.SysMachineStatusLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.entity.doc.Doc;
import com.yuzhyn.hidoc.app.application.entity.doc.DocCollected;
import com.yuzhyn.hidoc.app.application.entity.doc.DocCollectedMember;
import com.yuzhyn.hidoc.app.application.entity.doc.DocLite;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysMachineStatusLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocCollectedMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocCollectedMemberMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocMapper;
import com.yuzhyn.hidoc.app.application.model.UserInfo;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.common.model.ServiceException;
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
import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.azylee.core.logs.Alog;
import com.yuzhyn.azylee.core.threads.sleeps.Sleep;

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
    DocMapper docMapper;

    @Autowired
    SysMachineStatusLogMapper sysMachineStatusLogMapper;

    @Autowired
    SysUserLiteMapper sysUserLiteMapper;

    @PostMapping("create")
    public ResponseData create(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "name", "token")) {
            String name = MapTool.get(params, "name", "").toString();
            String description = MapTool.get(params, "description", "").toString();
            Boolean isOpen = MapTool.getBoolean(params, "isOpen", false);
            String token = MapTool.get(params, "token", "").toString();

            if (StringTool.ok(name, token)) {
                DocCollected docCollected = new DocCollected();
                docCollected.setId(R.SnowFlake.nexts());
                docCollected.setCreateTime(LocalDateTime.now());
                UserInfo userInfo = R.Cache.UserInfo.get(token);
                Alog.i("Thread.currentThread: " + Thread.currentThread());
                docCollected.setCreateUserId(userInfo.getUser().getId());
                docCollected.setOwnerUserId(userInfo.getUser().getId());
                docCollected.setName(name);
                docCollected.setDescription(description);
                docCollected.setIsOpen(isOpen);
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
            String token = MapTool.get(params, "token", "").toString();

            if (StringTool.ok(id, name, token)) {
                DocCollected record = docCollectedMapper.selectById(id);
                if (record != null) {
                    // 非所属者不能编辑
                    if (!record.getOwnerUserId().equals(CurrentUserManager.getUser().getId())) {
                        return ResponseData.error("编辑失败，非所属者不能编辑");
                    }

                    record.setName(name);
                    record.setDescription(description);
                    record.setIsOpen(isOpen);
                    int flag = docCollectedMapper.updateById(record);
                    if (flag > 0) {
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
                    int existDocCount = docLiteMapper.selectCount(new LambdaQueryWrapper<DocLite>().eq(DocLite::getCollectedId, id).eq(DocLite::getIsDelete, false));
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
        List<DocCollectedMember> memberList = docCollectedMemberMapper.selectList(new LambdaQueryWrapper<DocCollectedMember>().eq(DocCollectedMember::getUserId, CurrentUserManager.getUser().getId()));
        if (ListTool.ok(memberList)) {
            List<String> idList = memberList.stream().map(DocCollectedMember::getCollectedId).collect(Collectors.toList());
            if (ListTool.ok(idList)) {
                coopList = docCollectedMapper.selectBatchIds(idList);
                if (ListTool.ok(coopList)) {
                    for (int i = coopList.size() - 1; i >= 0; i--) {
                        if (coopList.get(i).getOwnerUserId().equals(CurrentUserManager.getUser().getId()) && coopList.get(i).getIsDelete()) {
                            coopList.remove(i);
                        }
                    }
                }
            }
        }

        // 设置协作标志
        if (ListTool.ok(mineList)) {
            for (DocCollected item : mineList) {
                Integer count = docCollectedMemberMapper.selectCount(new LambdaQueryWrapper<DocCollectedMember>()
                        .eq(DocCollectedMember::getCollectedId, item.getId())
                        .ne(DocCollectedMember::getUserId, item.getOwnerUserId()));
                if (count != null && count > 0) {
                    item.setIsCoop(true);
                }
            }
        }

        ResponseData responseData = ResponseData.ok();
        responseData.putDataMap("mine", mineList);
        responseData.putDataMap("coop", coopList);
        return responseData;
    }

    @PostMapping("preview")
    public ResponseData preview() {
        List<DocCollected> collectedList = docCollectedMapper.selectList(new LambdaQueryWrapper<DocCollected>().eq(DocCollected::getIsOpen, true));
        if (ListTool.ok(collectedList)) {
            for (DocCollected collected : collectedList) {
                Page<DocLite> page = new Page<>(1, 5);
                IPage<DocLite> docLites = docLiteMapper.selectPage(page, new LambdaQueryWrapper<DocLite>().eq(DocLite::getCollectedId, collected.getId()).orderByDesc(DocLite::getUpdateTime));
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
                collected.setDocLites(docLiteList);

                // 查询文集成员
                List<SysUserLite> sysUserLites = new ArrayList<>();
                // 查询文集拥有者
                SysUserLite ownerUser = sysUserLiteMapper.selectById(collected.getOwnerUserId());
                ownerUser.setMemberDesc("管理文集");
                sysUserLites.add(ownerUser);
                // 查询文集创建者
                SysUserLite createUser = sysUserLiteMapper.selectById(collected.getCreateUserId());
                createUser.setMemberDesc("创建文集");
                sysUserLites.add(createUser);
                // 查询文集包含的协作成员
                List<DocCollectedMember> docCollectedMembers = docCollectedMemberMapper.selectList(new LambdaQueryWrapper<DocCollectedMember>().eq(DocCollectedMember::getCollectedId, id));
                if (ListTool.ok(docCollectedMembers)) {
                    List<String> userIds = docCollectedMembers.stream().map(DocCollectedMember::getUserId).collect(toList());
                    List<SysUserLite> _userList = sysUserLiteMapper.selectBatchIds(userIds);
                    if (ListTool.ok(_userList)) {
                        for (SysUserLite item : _userList) {
                            if (!item.getId().equals(ownerUser.getId())) {
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

            List<DocLite> list = docLiteMapper.selectList(new LambdaQueryWrapper<DocLite>()
                    .eq(DocLite::getCollectedId, collectedId)
                    .eq(DocLite::getIsDelete, false)
                    .orderByAsc(DocLite::getSerialNumber, DocLite::getCreateTime));
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
                for (DocLite item : list) {
                    docLiteMapper.updateById(item);
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
                    if (ListTool.ok(otherUserIdList) && ListTool.ok(memberIdList)) otherUserIdList.removeAll(memberIdList);
                    if (ListTool.ok(otherUserIdList)) otherUserList = sysUserLiteMapper.selectBatchIds(otherUserIdList);
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

    @Transactional
    @PostMapping("test")
    public ResponseData test() throws Exception {
        // 创建某一类型的数据
        SysMachineStatusLog sl = new SysMachineStatusLog();
        sl.setId(R.SnowFlake.nexts());
        sl.setMachineId("90909");
        sl.setCreateTime(LocalDateTime.now());
        sl.setCpu(0);
        sl.setRam(0L);
        sl.setDisk(0L);
        sl.setAppCpu(0);
        sl.setAppRam(0L);
        sl.setSsLong(UUIDTool.getId1024());
        sysMachineStatusLogMapper.insert(sl);

        for (int i = 0; i <= 10000; i++) {
            SysMachineStatusLog rec = sysMachineStatusLogMapper.selectById(sl.getId());
            rec.setAppRam(Long.valueOf(i));
            sysMachineStatusLogMapper.updateById(rec);
            Sleep.s(1);
        }
        throw new Exception("强制退出");
//        return ResponseData.ok();
    }

    @Transactional
    @PostMapping("test2")
    public ResponseData test2() {
        // 创建某一类型的数据
        SysMachineStatusLog sl = new SysMachineStatusLog();
        sl.setId(R.SnowFlake.nexts());
        sl.setMachineId("90909");
        sl.setCreateTime(LocalDateTime.now());
        sl.setCpu(0);
        sl.setRam(0L);
        sl.setDisk(0L);
        sl.setAppCpu(0);
        sl.setAppRam(0L);
        sl.setSsLong(UUIDTool.getId1024());
        sysMachineStatusLogMapper.insert(sl);

        for (int i = 0; i <= 10000; i++) {
            SysMachineStatusLog rec = sysMachineStatusLogMapper.selectById(sl.getId());
            rec.setAppRam(Long.valueOf(i));
            sysMachineStatusLogMapper.updateById(rec);
            Sleep.s(1);
            checkData(rec);
        }
        return ResponseData.ok();
    }

    private void checkData(SysMachineStatusLog data) {
        if (data.getCpu() == 0) {
            throw new ServiceException("强制退出");
        }
    }
}
