package com.yuzhyn.hidoc.app.application.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
import com.yuzhyn.azylee.core.datas.regexs.RegexPattern;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.file.FileBucket;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursorView;
import com.yuzhyn.hidoc.app.application.entity.sys.SysAccessLog;
import com.yuzhyn.hidoc.app.application.mapper.file.FileBucketMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileCursorMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileCursorViewMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysAccessLogMapper;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("test")
public class TestController {


    @Autowired
    SysAccessLogMapper sysAccessLogMapper;

    @GetMapping({"export"})
    public ResponseData export() {

        String fileName = DirTool.combine(R.Paths.Temp, UUIDTool.get() + ".xlsx");
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        ExcelWriter excelWriter = EasyExcel.write(fileName, SysAccessLog.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("Sheet1").head(SysAccessLog.class).build();

        //region 分批处理
        for (int i = 0; i < 500; i++) {
            List<SysAccessLog> list = sysAccessLogMapper.selectList(null);
            fillData(list);
            excelWriter.write(list, writeSheet);
        }
        //endregion

//        //region 全量处理
//        List list = new ArrayList();
//        for (int i = 0; i < 500; i++) {
//            List<SysAccessLog> _l = sysAccessLogMapper.selectList(null);
//            list.addAll(_l);
//        }
//        fillData(list);
//        excelWriter.write(list, writeSheet);
//        //endregion

        excelWriter.finish();

        return ResponseData.ok();
    }

    private List fillData(List<SysAccessLog> list) {
        for (SysAccessLog item : list) {
            item.setS1(UUIDTool.get());
            item.setS2(UUIDTool.get());
            item.setS3(UUIDTool.get());
            item.setS4(UUIDTool.get());
            item.setS5(UUIDTool.get());
            item.setS6(UUIDTool.get());
            item.setS7(UUIDTool.get());
            item.setS8(UUIDTool.get());
            item.setS9(UUIDTool.get());
            item.setS10(UUIDTool.get());
            item.setS11(UUIDTool.get());
            item.setS12(UUIDTool.get());
            item.setS13(UUIDTool.get());
            item.setS14(UUIDTool.get());
            item.setS15(UUIDTool.get());
            item.setS16(UUIDTool.get());
            item.setS17(UUIDTool.get());
            item.setS18(UUIDTool.get());
            item.setS19(UUIDTool.get());
            item.setS20(UUIDTool.get());
        }
        return list;
    }
}
