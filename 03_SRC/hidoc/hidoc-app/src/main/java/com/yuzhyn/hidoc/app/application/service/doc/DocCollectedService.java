package com.yuzhyn.hidoc.app.application.service.doc;

import com.vdurmont.emoji.EmojiParser;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.hidoc.app.application.entity.doc.DocCollected;
import com.yuzhyn.hidoc.app.application.entity.doc.DocLite;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocCollectedService {

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
