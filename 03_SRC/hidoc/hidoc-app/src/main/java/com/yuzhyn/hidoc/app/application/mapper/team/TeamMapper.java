package com.yuzhyn.hidoc.app.application.mapper.team;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursorView;
import com.yuzhyn.hidoc.app.application.entity.team.Team;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TeamMapper extends BaseMapper<Team> {


    /**
     * 查询其他团队信息（所属权不属于我的 AND 我未参与的）
     *
     * @param userId
     * @return
     */
    @Select("SELECT * FROM team WHERE owner_user_id != #{userId} AND id NOT IN (SELECT team_id FROM team_member WHERE user_id = #{userId})")
    List<Team> selectOthers(@Param("userId") String userId);
}