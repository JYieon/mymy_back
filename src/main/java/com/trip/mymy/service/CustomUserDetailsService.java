package com.trip.mymy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.dto.token.CustomUserDetails;
import com.trip.mymy.mybatis.AuthMapper;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired AuthMapper am;
	
	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		MemberDTO member = am.getUser(id);
		
		if(member == null) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + id);
		}
		
		return new CustomUserDetails(member);
	}
	
}
