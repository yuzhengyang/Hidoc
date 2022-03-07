package com.yuzhyn.hidoc.app.application.model.javadoc;

import lombok.Data;

@Data
public class JavaDocCommentStruct {
    String type;
    String txt;

    public JavaDocCommentStruct(String _type, String _txt) {
        this.type = _type;
        this.txt = _txt;
    }
}
