package com.yuzhyn.hidoc.app.application.service.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.sys.SysAccessLog;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysAccessLogMapper;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class TestEasyExcelService {

    @Autowired
    SysAccessLogMapper sysAccessLogMapper;

    public void writeExcel(String filepath) {

        System.out.println("创建Excel并开始填充数据：" + LocalDateTime.now());
        ExcelWriter excelWriter = EasyExcel.write(filepath, SysAccessLog.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("Sheet1").build();

        System.out.println("写入数据：" + LocalDateTime.now());
        // excel单sheet页最大支持104万条数据
        sysAccessLogMapper.selectAllLog(new HashMap<>(), resultContext -> {
            SysAccessLog obj = resultContext.getResultObject();
            // 将数据写入Excel
            excelWriter.write(Arrays.asList(obj), writeSheet);
        });
        System.out.println("将工作簿写入文件：" + LocalDateTime.now());
        excelWriter.finish();
    }
}
