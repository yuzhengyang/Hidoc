package com.yuzhyn.hidoc.app.application.mapper.team;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursorView;
import com.yuzhyn.hidoc.app.application.entity.team.Team;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TeamMapper extends BaseMapper<Team> {

    @Update("UPDATE team SET member_count = (SELECT COUNT(1) FROM team_member WHERE team_id = #{teamId}) WHERE id = #{teamId}")
    Integer updateMemberCount(@Param("teamId") String teamId);
}