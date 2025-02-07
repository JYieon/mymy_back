package com.trip.mymy.mybatis;

import org.apache.ibatis.annotations.Mapper;

import com.trip.mymy.dto.MemberDTO;

@Mapper
public interface AuthMapper {
	public MemberDTO getUser(String id);
}
