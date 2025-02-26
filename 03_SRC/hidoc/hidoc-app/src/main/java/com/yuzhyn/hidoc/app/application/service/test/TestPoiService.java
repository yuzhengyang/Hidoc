package com.yuzhyn.hidoc.app.application.service.test;

import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.application.entity.sys.SysAccessLog;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysAccessLogMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TestPoiService {

    @Autowired
    SysAccessLogMapper sysAccessLogMapper;

    public void writeExcel(String filepath) {
        System.out.println("创建Excel并开始填充数据：" + LocalDateTime.now());
        SXSSFWorkbook wb = new SXSSFWorkbook(2000);

        String sheetName = "Sheet1";
        SXSSFSheet sheet = wb.createSheet(sheetName);

        System.out.println("写入数据：" + LocalDateTime.now());
        // excel单sheet页最大支持104万条数据
        AtomicInteger rowIndex = new AtomicInteger();

        sysAccessLogMapper.selectAllLog(new HashMap<>(), resultContext -> {
            /*
             * 这里根据模型字段，写入到Excel中
             * String id;
             * String ip;
             * LocalDateTime beginTime;
             * LocalDateTime endTime;
             * String uri;
             * String method;
             * Long elapsedTime;
             * String userId;
             * String step;
             * String threadName;
             * Long beginMaxMemory;
             * Long beginTotalMemory;
             * Long beginFreeMemory;
             * Long endMaxMemory;
             * Long endTotalMemory;
             * Long endFreeMemory;
             * Long probablyUseMemory;
             * String exception;
             * String machineId;
             * String pointId;
             * String accessOrigin;
             * */
            SysAccessLog obj = resultContext.getResultObject();
            Row dataRow = sheet.createRow(rowIndex.getAndIncrement());

            Cell idCell = dataRow.createCell(0);
            idCell.setCellValue(StringTool.ok(obj.getId()) ? obj.getId() : "");

            Cell ipCell = dataRow.createCell(1);
            ipCell.setCellValue(StringTool.ok(obj.getIp()) ? obj.getIp() : "");

            Cell beginTimeCell = dataRow.createCell(2);
            beginTimeCell.setCellValue(obj.getBeginTime() != null ? obj.getBeginTime().toString() : "");

            Cell endTimeCell = dataRow.createCell(3);
            endTimeCell.setCellValue(obj.getEndTime() != null ? obj.getEndTime().toString() : "");

            Cell uriCell = dataRow.createCell(4);
            uriCell.setCellValue(StringTool.ok(obj.getUri()) ? obj.getUri() : "");

            Cell methodCell = dataRow.createCell(5);
            methodCell.setCellValue(StringTool.ok(obj.getMethod()) ? obj.getMethod() : "");

            Cell elapsedTimeCell = dataRow.createCell(6);
            elapsedTimeCell.setCellValue(obj.getElapsedTime() != null ? obj.getElapsedTime() : 0);

            Cell userIdCell = dataRow.createCell(7);
            userIdCell.setCellValue(StringTool.ok(obj.getUserId()) ? obj.getUserId() : "");

            Cell stepCell = dataRow.createCell(8);
            stepCell.setCellValue(StringTool.ok(obj.getStep()) ? obj.getStep() : "");

            Cell threadNameCell = dataRow.createCell(9);
            threadNameCell.setCellValue(StringTool.ok(obj.getThreadName()) ? obj.getThreadName() : "");

            Cell beginMaxMemoryCell = dataRow.createCell(10);
            beginMaxMemoryCell.setCellValue(obj.getBeginMaxMemory() != null ? obj.getBeginMaxMemory() : 0);

            Cell beginTotalMemoryCell = dataRow.createCell(11);
            beginTotalMemoryCell.setCellValue(obj.getBeginTotalMemory() != null ? obj.getBeginTotalMemory() : 0);

            Cell beginFreeMemoryCell = dataRow.createCell(12);
            beginFreeMemoryCell.setCellValue(obj.getBeginFreeMemory() != null ? obj.getBeginFreeMemory() : 0);

            Cell endMaxMemoryCell = dataRow.createCell(13);
            endMaxMemoryCell.setCellValue(obj.getEndMaxMemory() != null ? obj.getEndMaxMemory() : 0);

            Cell endTotalMemoryCell = dataRow.createCell(14);
            endTotalMemoryCell.setCellValue(obj.getEndTotalMemory() != null ? obj.getEndTotalMemory() : 0);

            Cell endFreeMemoryCell = dataRow.createCell(15);
            endFreeMemoryCell.setCellValue(obj.getEndFreeMemory() != null ? obj.getEndFreeMemory() : 0);

            Cell probablyUseMemoryCell = dataRow.createCell(16);
            probablyUseMemoryCell.setCellValue(obj.getProbablyUseMemory() != null ? obj.getProbablyUseMemory() : 0);

            Cell exceptionCell = dataRow.createCell(17);
            exceptionCell.setCellValue(StringTool.ok(obj.getException()) ? obj.getException() : "");

            Cell machineIdCell = dataRow.createCell(18);
            machineIdCell.setCellValue(StringTool.ok(obj.getMachineId()) ? obj.getMachineId() : "");

            Cell pointIdCell = dataRow.createCell(19);
            pointIdCell.setCellValue(StringTool.ok(obj.getPointId()) ? obj.getPointId() : "");

            Cell accessOriginCell = dataRow.createCell(20);
            accessOriginCell.setCellValue(StringTool.ok(obj.getAccessOrigin()) ? obj.getAccessOrigin() : "");

        });

        System.out.println("将工作簿写入文件：" + LocalDateTime.now());
        try (FileOutputStream fileOut = new FileOutputStream(filepath)) {
            wb.write(fileOut);
            System.out.println("Excel 文件已成功导出：" + LocalDateTime.now());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
