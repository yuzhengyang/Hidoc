package com.yuzhyn.hidoc.app.application.service.javadoc;

import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.datas.exceptions.ExceptionTool;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.azylee.core.ios.files.FileFindTool;
import com.yuzhyn.azylee.core.ios.files.FileTool;
import com.yuzhyn.azylee.core.ios.zips.ZipTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class JavaDocUploadService {

    @Autowired
    JavaDocCreateService javaDocCreateService;

    @Autowired
    JavaDocCleanService javaDocCleanService;

    public ResponseData uploadZip(MultipartFile file) {
        ResponseData responseData;
        List<String> step = new ArrayList<>();
        try {
            step.add("创建临时文件目录");
            String tempPath = DirTool.combine(R.Paths.Temp, DateTimeFormat.toStr(LocalDateTime.now(), DateTimeFormatPattern.SHORT_DATE));
            DirTool.create(tempPath);

            step.add("临时存放上传的zip文件");
            String fileNameNoExt = R.SnowFlake.nexts();
            String tempFilePath = DirTool.combine(tempPath, fileNameNoExt + ".zip");
            java.io.File dest = new java.io.File(tempFilePath);
            file.transferTo(dest);

            step.add("解压缩zip");
            ZipTool.unzip(tempFilePath, tempPath, fileNameNoExt);

            step.add("搜索所有java文件");
            List<String> javaFileList = FileFindTool.getAllFiles(DirTool.combine(tempPath, fileNameNoExt), ".java");

            // 搜索并查找project信息
            if (ListTool.ok(javaFileList)) {
                String zipFileName = FileTool.getNameWithoutExt(file.getOriginalFilename());

                step.add("重新解析并构建注释文档");
                responseData = javaDocCreateService.uploadZip(zipFileName, javaFileList, step);

                step.add("打扫现场，删除临时文件");
                if (tempFilePath.startsWith(R.Paths.Temp)) FileTool.delete(tempFilePath);
                if (tempPath.startsWith(R.Paths.Temp)) DirTool.delete(DirTool.combine(tempPath, fileNameNoExt));

                step.add("完成所有工作");
                responseData.putDataMap("step", step);
                return responseData;
            } else {
                responseData = ResponseData.error("没有找到java文件");
                responseData.putDataMap("step", step);
                return responseData;
            }
        } catch (Exception ex) {
            responseData = ResponseData.error("zip文件分析失败");
            responseData.putDataMap("step", step);
            responseData.putDataMap("exception", ExceptionTool.getStackTrace(ex));
            return responseData;
        }
    }
}
