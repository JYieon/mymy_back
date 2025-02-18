package com.trip.mymy.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.trip.mymy.dto.MemberDTO;

@Mapper
public interface AuthMapper {
	public MemberDTO getUser(String id);
	public int insertUser(MemberDTO dto);
	public MemberDTO findId(@Param("name") String name, @Param("email") String email);
	public MemberDTO findPwd(@Param("id") String id, @Param("email") String email);
	public int resetPwd(@Param("id") String id, @Param("pwd") String pwd);
}
