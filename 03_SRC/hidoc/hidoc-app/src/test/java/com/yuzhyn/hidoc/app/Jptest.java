package com.yuzhyn.hidoc.app;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class Jptest {
    public static void main(String[] args) throws FileNotFoundException {
        // parse() 参数可以是 String, File, InputStream等
        String filepath = "D:\\code\\projects\\haixin-projects\\hicmp-utils\\hicmp-utils\\hicmp-common-hiutils\\src\\main\\java\\com\\histone\\hiutils\\hiDbUtils\\hiDbAssistUtils\\HiDbModel.java";
        CompilationUnit cu = StaticJavaParser.parse(new File(filepath));
        List<TypeDeclaration> cds = cu.findAll(TypeDeclaration.class);
        List<MethodDeclaration> mds = cu.findAll(MethodDeclaration.class);
        mds.forEach(md -> System.out.println(md.toString() + "\n------------------------------\n"));
    }
}
