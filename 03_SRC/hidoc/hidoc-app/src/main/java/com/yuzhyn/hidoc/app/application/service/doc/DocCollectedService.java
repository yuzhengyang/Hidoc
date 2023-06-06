package com.yuzhyn.hidoc.app.application.service.doc;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vdurmont.emoji.EmojiParser;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.application.entity.doc.DocCollected;
import com.yuzhyn.hidoc.app.application.entity.doc.DocCollectedMember;
import com.yuzhyn.hidoc.app.application.entity.doc.DocLite;
import com.yuzhyn.hidoc.app.application.entity.team.TeamMember;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocCollectedMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocCollectedMemberMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.team.TeamMemberMapper;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class DocCollectedService {
    @Autowired
    DocLiteMapper docLiteMapper;
    @Autowired
    DocCollectedMapper docCollectedMapper;
    @Autowired
    TeamMemberMapper teamMemberMapper;
    @Autowired
    DocCollectedMemberMapper docCollectedMemberMapper;

    public List<DocCollected> search(Map<String, Object> params) {
        // 支撑关键字搜索
        int pageSize = 5;
        String keyword = MapTool.get(params, "keyword", "").toString();
        String from = MapTool.get(params, "from", "").toString();
        Boolean isTemplet = MapTool.getBoolean(params, "isTemplet", false);
        String collectedRole = MapTool.getString(params, "collectedRole", "");

        String[] keywordArray = StringTool.split(keyword, " ", true, true);
        List<String> collectedIds = new ArrayList<>();
        List<String> docIds = new ArrayList<>();

        // 根据关键字筛选有效信息
        if (StringTool.ok(keyword)) {
            collectedIds.add("*not_exist");
            docIds.add("*not_exist");
            pageSize = 20;

            if (from.equals("editor")) pageSize = 100;
            // 搜索匹配的文档集合
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
                        // 整理文档结果id列表
                        docIds.addAll(preDocList.stream().map(DocLite::getId).distinct().collect(toList()));
                        // 整理文集结果id列表
                        collectedIds.addAll(preDocList.stream().map(DocLite::getCollectedId).distinct().collect(toList()));
                    }
                }
            }
        }

        List<DocCollected> collectedList = new ArrayList<>();
        switch (collectedRole) {
            case "myJoin": {
                if (CurrentUserManager.isLogin()) {
                    // 获取我加入的协作（如果是我创建的，但是也放到了协作成员里，也是可以搜到的）
                    List<String> memberCollectedIds = null;
                    List<DocCollectedMember> memberList = docCollectedMemberMapper.selectList(new LambdaQueryWrapper<DocCollectedMember>().eq(DocCollectedMember::getUserId, CurrentUserManager.getUser().getId()));
                    if (ListTool.ok(memberList)) {
                        memberCollectedIds = memberList.stream().map(DocCollectedMember::getCollectedId).collect(Collectors.toList());
                        // 只有协作中有我的，才查询出来
                        LambdaQueryWrapper<DocCollected> docCollectedLambdaQueryWrapper = new LambdaQueryWrapper<DocCollected>()
                                .eq(DocCollected::getIsDelete, false)
                                .eq(DocCollected::getIsTemplet, isTemplet);
                        if (ListTool.ok(collectedIds))
                            docCollectedLambdaQueryWrapper = docCollectedLambdaQueryWrapper.in(DocCollected::getId, collectedIds);
                        if (ListTool.ok(memberCollectedIds))
                            docCollectedLambdaQueryWrapper = docCollectedLambdaQueryWrapper.in(DocCollected::getId, memberCollectedIds);

                        collectedList = docCollectedMapper.selectList(docCollectedLambdaQueryWrapper);
                    }
                }
                break;
            }
            case "private": {
                if (CurrentUserManager.isLogin()) {
                    // 此处框定返回内容
                    LambdaQueryWrapper<DocCollected> docCollectedLambdaQueryWrapper = new LambdaQueryWrapper<DocCollected>()
                            .eq(DocCollected::getIsOpen, false)
                            .eq(DocCollected::getIsDelete, false)
                            .eq(DocCollected::getIsTemplet, isTemplet)
                            .eq(DocCollected::getOwnerUserId, CurrentUserManager.getUserId());
                    if (ListTool.ok(collectedIds))
                        docCollectedLambdaQueryWrapper = docCollectedLambdaQueryWrapper.in(DocCollected::getId, collectedIds);

                    collectedList = docCollectedMapper.selectList(docCollectedLambdaQueryWrapper);
                }
                break;
            }
            case "default":
            default: {
                // 获取当前用户的团队关系
                List<String> teamIds = new ArrayList<>();
                if (CurrentUserManager.isLogin()) {
                    List<TeamMember> memberList = teamMemberMapper.selectList(new LambdaQueryWrapper<TeamMember>().eq(TeamMember::getUserId, CurrentUserManager.getUserId()));
                    if (ListTool.ok(memberList))
                        teamIds.addAll(memberList.stream().map(TeamMember::getTeamId).distinct().collect(toList()));
                }

                // 此处框定返回内容
                LambdaQueryWrapper<DocCollected> docCollectedLambdaQueryWrapper = new LambdaQueryWrapper<DocCollected>()
                        .eq(DocCollected::getIsOpen, true)
                        .eq(DocCollected::getIsDelete, false)
                        .eq(DocCollected::getIsTemplet, isTemplet)
                        .and(p -> {
                            p.or().apply("team_id_list IS NULL OR team_id_list = '[]'");
                            for (String teamId : teamIds) {
                                p.or().apply("jsonb_exists(team_id_list, {0})", teamId);
                            }
                        });
                if (ListTool.ok(collectedIds))
                    docCollectedLambdaQueryWrapper = docCollectedLambdaQueryWrapper.in(DocCollected::getId, collectedIds);

                collectedList = docCollectedMapper.selectList(docCollectedLambdaQueryWrapper);
                break;
            }
        }

        if (ListTool.ok(collectedList)) {
            for (DocCollected collected : collectedList) {
                Page<DocLite> page = new Page<>(1, pageSize);
                // 此处根据关键字筛选的信息来框定返回内容
                LambdaQueryWrapper<DocLite> docLiteLambdaQueryWrapper = new LambdaQueryWrapper<DocLite>().eq(DocLite::getCollectedId, collected.getId()).eq(DocLite::getIsDelete, false).orderByDesc(DocLite::getUpdateTime);
                if (ListTool.ok(docIds))
                    docLiteLambdaQueryWrapper = docLiteLambdaQueryWrapper.in(DocLite::getId, docIds);

                IPage<DocLite> docLites = docLiteMapper.selectPage(page, docLiteLambdaQueryWrapper);
                if (docLites.getTotal() > 0) {
                    collected.setDocLites(docLites.getRecords());
                }
            }
            for (int i = collectedList.size() - 1; i >= 0; i--) {
                if (!ListTool.ok(collectedList.get(i).getDocLites())) {
                    collectedList.remove(i);
                }
            }
            // 文集格式化名称，并排序
            sortByFormatName(collectedList);
        }
        return collectedList;
    }

    public static void sortByFormatName(List<DocCollected> collectedList) {
        if (!ListTool.ok(collectedList)) return;

        for (int i = collectedList.size() - 1; i >= 0; i--) {
            // 格式化名称
            try {
                collectedList.get(i).setFormatName(EmojiParser.removeAllEmojis(collectedList.get(i).getName()).trim());
            } catch (Exception ex) {
            }
        }
        // 文集按格式化后的名称排序
        collectedList.sort((o1, o2) -> {
            return o1.getFormatName().compareTo(o2.getFormatName());
        });
    }
}
