package com.yuzhyn.hidoc.app.application.model.doc;

import com.yuzhyn.azylee.core.datas.collections.ArrayTool;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DocLinkVo {
    private int sourceNo;
    private String collectedId;
    private String collectedName;
    private String docId;
    private String docTitle;

    private int targetNo;
    private String linkName;
    private String linkCollectedId;
    private String linkCollectedName;
    private String linkDocId;
    private String linkDocTitle;

    public static DocLinkVo create(String s) {
        DocLinkVo vo = null;

        String[] splitNameAndLink = StringTool.splitLine(s, "\\]\\(\\#hd\\.ilink->", 2, "", true);
        // 拆分名称和链接部分
        String linkName = ArrayTool.get(splitNameAndLink, 0, "");
        String linkUrl = ArrayTool.get(splitNameAndLink, 1, "");
        if (StringTool.ok(linkName)) linkName = linkName.substring(1);
        if (StringTool.ok(linkUrl)) linkUrl = linkUrl.substring(0, linkUrl.length() - 1);

        String[] linkUrlArray = StringTool.splitLine(linkUrl, "/", 2, "", true);
        String linkCollectedId = linkUrlArray[0];
        String linkDocId = linkUrlArray[1];

        if (StringTool.ok(linkName, linkCollectedId, linkDocId)) {
            vo = new DocLinkVo();
            vo.setLinkName(linkName);
            vo.setLinkCollectedId(linkCollectedId);
            vo.setLinkDocId(linkDocId);
        }
        return vo;
    }

    public static Map<String, Object> parseList(List<DocLinkVo> docLinkVoList) {
        Map<String, Object> result = new HashMap<>();

        Map<String, Integer> datasDic = new HashMap<>();
        List<Map<String, Object>> datas = new ArrayList<>();
        List<Map<String, Object>> links = new ArrayList<>();

        if (ListTool.ok(docLinkVoList)) {
            for (DocLinkVo item : docLinkVoList) {
                // 归集文档数据列表
                if (!datasDic.containsKey(item.getDocId())) {
                    Map<String, Object> doc = new HashMap<>();
                    doc.put("value", 1);
                    doc.put("name", item.getDocTitle());
                    doc.put("category", 1);
                    doc.put("url", "");
                    doc.put("collectedId", item.getCollectedId());
                    doc.put("docId", item.getDocId());
                    datas.add(doc);
                    datasDic.put(item.getDocId(), datas.size() - 1);
                }
                if (!datasDic.containsKey(item.getLinkDocId())) {
                    Map<String, Object> doc = new HashMap<>();
                    doc.put("value", 1);
                    doc.put("name", item.getLinkDocTitle());
                    doc.put("category", 1);
                    doc.put("url", "");
                    doc.put("collectedId", item.getLinkCollectedId());
                    doc.put("docId", item.getLinkDocId());
                    datas.add(doc);
                    datasDic.put(item.getLinkDocId(), datas.size() - 1);
                }

                // 归集链接数据列表
                Integer srcNo = datasDic.get(item.getDocId());
                Integer tarNo = datasDic.get(item.getLinkDocId());

                Map<String, Object> link = new HashMap<>();
                link.put("source", srcNo);
                link.put("target", tarNo);
                links.add(link);
            }
        }
        result.put("datas", datas);
        result.put("links", links);
        return result;
    }

    public static void main(String[] args) {
        DocLinkVo vo = DocLinkVo.create("[\uD83E\uDDF0 工具软件-数据库工具：DBeaver](#hd.ilink->121981462344892416/121983064824872960)");
        if (vo != null) {
            System.out.println(vo.getLinkName());
            System.out.println(vo.getLinkCollectedId());
            System.out.println(vo.getLinkDocId());
        }
    }
}
