package com.yuzhyn.hidoc.app.application.mapper.team;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuzhyn.hidoc.app.application.entity.file.HidocFileView;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.entity.team.TeamMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TeamMemberMapper extends BaseMapper<TeamMember> {
    @Select("SELECT id,name,real_name,avatar,email,is_frozen,create_time,online_time,login_time,vip_level,roles " +
            "FROM sys_user " +
            "WHERE is_frozen = false AND id NOT IN (SELECT user_id FROM team_member WHERE team_id = #{teamId})")
    List<SysUserLite> selectUnJoinUsers(@Param("teamId") String teamId);
}