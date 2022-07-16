package com.yuzhyn.hidoc.app.application.controller4openapi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.exceptions.ExceptionTool;
import com.yuzhyn.azylee.core.datas.regexs.RegexTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.application.entity.doc.Doc;
import com.yuzhyn.hidoc.app.application.entity.doc.DocCollected;
import com.yuzhyn.hidoc.app.application.entity.doc.DocLite;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocCollectedMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.model.doc.DocLinkVo;
import com.yuzhyn.hidoc.app.application.service.sys.AuthCodeService;
import com.yuzhyn.hidoc.app.application.service.sys.EmailService;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.util.function.Tuple2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("openapi/collected")
public class DocCollectedApiController {

    @Autowired
    DocCollectedMapper docCollectedMapper;

    @Autowired
    DocMapper docMapper;

    @Autowired
    DocLiteMapper docLiteMapper;

    /**
     * 获取文集引用文档关系信息
     *
     * @param params
     * @return
     */
    @PostMapping("ilinkRelation")
    public ResponseData ilinkRelation(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        if (MapTool.ok(params, "id")) {
            String id = MapTool.getString(params, "id", "");

            // 查询文集和文集中的文档
            DocCollected docCollected = docCollectedMapper.selectById(id);
            List<Doc> docList = docMapper.selectList(new LambdaQueryWrapper<Doc>().eq(Doc::getIsDelete, false).eq(Doc::getCollectedId, id));
            if (docCollected != null && ListTool.ok(docList)) {

                // 解析所有文档中的所有引用文档信息
                List<DocLinkVo> docLinkVoList = new ArrayList<>();
                for (Doc docItem : docList) {
                    if (StringTool.ok(docItem.getContent())) {
                        List<String> linkList = RegexTool.getMatchs(docItem.getContent(), "\\[.*?\\]\\(#hd\\.ilink->.*?\\)");
                        for (String sItem : linkList) {
                            DocLinkVo vo = DocLinkVo.create(sItem);
                            if (vo != null) {
                                vo.setCollectedId(id);
                                vo.setCollectedName(docCollected.getName());
                                vo.setDocId(docItem.getId());
                                vo.setDocTitle(docItem.getTitle());
                                docLinkVoList.add(vo);
                            }
                        }
                    }
                }

                // 补充相关信息
                if (ListTool.ok(docLinkVoList)) {
                    List<String> linkCollectedIds = docLinkVoList.stream().map(DocLinkVo::getLinkCollectedId).distinct().collect(toList());
                    List<String> linkDocIds = docLinkVoList.stream().map(DocLinkVo::getLinkDocId).distinct().collect(toList());

                    List<DocCollected> linkCollectedList = docCollectedMapper.selectBatchIds(linkCollectedIds);
                    List<DocLite> linkDocList = docLiteMapper.selectBatchIds(linkDocIds);

                    for (DocLinkVo voItem : docLinkVoList) {
                        for (DocCollected colItem : linkCollectedList) {
                            if (voItem.getLinkCollectedId().equals(colItem.getId())) {
                                voItem.setLinkCollectedName(colItem.getName());
                            }
                        }
                        for (DocLite docItem : linkDocList) {
                            if (voItem.getLinkDocId().equals(docItem.getId())) {
                                voItem.setLinkDocTitle(docItem.getTitle());
                            }
                        }
                    }
                }
                Map parseData = DocLinkVo.parseList(docLinkVoList);
                responseData.putDataMap("datas",parseData.get("datas"));
                responseData.putDataMap("links",parseData.get("links"));
            }
        }
        return responseData;
    }
}