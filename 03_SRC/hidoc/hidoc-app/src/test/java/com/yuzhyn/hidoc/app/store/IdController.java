package com.yuzhyn.hidoc.app.store;

import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.service.IdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping({"i/id"})
public class IdController {

    @Autowired
    IdService idService;

    @GetMapping("/uuid")
    public String uuid() {
        return idService.getId();
    }

    @GetMapping("/snow")
    public String snow() {
        return String.valueOf(R.SnowFlake.nexts());
    }



//    @GetMapping("/get/{count}")
//    public ResponseData get(@PathVariable("count") int count) {
//        ArrayList<Long> ids = new ArrayList();
//        SnowflakeTool idWorker = new SnowflakeTool(1, 1, 1);
//        if (count <= 0) {
//            count = 1;
//        }
//        for (int i = 0; i < count; i++) {
//            ids.add(idWorker.nexts());
//        }
//        return ResponseData.ok(ids);
//    }
}
