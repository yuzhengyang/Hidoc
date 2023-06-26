package com.yuzhyn.hidoc.app.application.service.file;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.file.FileBucket;
import com.yuzhyn.hidoc.app.application.mapper.file.FileBucketMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class FileBucketService {

    @Autowired
    FileBucketMapper fileBucketMapper;

    /**
     * 创建系统默认的文件夹(.hidoc .avatar)，不能用作用户普通文件夹的创建
     *
     * @param name
     * @param userId
     */
    public void createSystemDefaultBucket(String name, String userId) {
        if (fileBucketMapper.selectCount(new LambdaQueryWrapper<FileBucket>().eq(FileBucket::getUserId, userId).eq(FileBucket::getName, name)) == 0) {
            FileBucket fileBucket = new FileBucket();
            fileBucket.setId(R.SnowFlake.nexts());
            fileBucket.setIsOpen(true);
            fileBucket.setName(name);
            fileBucket.setUserId(userId);
            fileBucketMapper.insert(fileBucket);
        }
    }
}
