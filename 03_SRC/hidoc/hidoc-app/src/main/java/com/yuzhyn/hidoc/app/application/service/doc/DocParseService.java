package com.yuzhyn.hidoc.app.application.service.doc;

import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.application.entity.doc.Doc;
import com.yuzhyn.hidoc.app.application.entity.doc.DocLite;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocMenu;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocParseService {

    public List<DocLite> docLite2Tree(List<DocLite> docList) {
        for (DocLite item : docList) {
            item.setCurrentTopLevel(true);
        }
        for (DocLite item : docList) {
            item.setLabel(item.getTitle());
            item.setChildren(new ArrayList<>());

            for (DocLite childrenItem : docList) {
                if(childrenItem.getParentDocId() == null) childrenItem.setParentDocId("");
                if (childrenItem.getParentDocId().equals(item.getId())) {
                    item.getChildren().add(childrenItem);
                    childrenItem.setCurrentTopLevel(false);
                }
            }
        }
//        return docList.stream().filter(x -> x.getParentDocId().isEmpty()).collect(Collectors.toList());
        return docList.stream().filter(x -> x.getCurrentTopLevel()).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        DocLite docLite1 = new DocLite();
        docLite1.setId("xuexiao");
        docLite1.setTitle("学校");

        DocLite docLite2 = new DocLite();
        docLite2.setId("banji");
        docLite2.setTitle("班级");
        docLite2.setParentDocId("xuexiao");

        DocLite docLite3 = new DocLite();
        docLite3.setId("xuesheng");
        docLite3.setTitle("学生");
        docLite3.setParentDocId("banji");

        List<DocLite> list = Arrays.asList(docLite1, docLite2, docLite3);
//        list = docLite2Tree(list);

        int i = 0;

    }
}
