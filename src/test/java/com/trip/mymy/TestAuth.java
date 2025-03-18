package com.trip.mymy;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.mymy.controller.AuthController;
import com.trip.mymy.dto.LoginReqDTO;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.mybatis.AuthMapper;
import com.trip.mymy.service.AuthService;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"classpath:testAuth.xml", 
		"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class TestAuth {
	
	@Autowired AuthController lc;
	@Autowired AuthService as;
	@Autowired AuthMapper am;
	
	MockMvc mock;
	
	@Before //org.junit.before
	public void setUp() {
		System.out.println("--- setUp 실행 ---");
		mock = MockMvcBuilders.standaloneSetup(lc).build();
	}
	
	@Test
	public void testLc() throws Exception{
		LoginReqDTO loginreq = new LoginReqDTO("test", "test123");
		String jsonRequest = new ObjectMapper().writeValueAsString(loginreq);
		
		 mock.perform(post("/auth/login")
				 .contentType(MediaType.APPLICATION_JSON)
				    .content(jsonRequest))
				    .andDo(print())
				    .andExpect(status().isOk());
	}
	
	@Test
	public void testId() throws Exception{
		 mock.perform(post("/find_id")
		 	.param("name", "테스트")
		 	.param("email", "dkcl.8642@gmail.com"))
		    .andDo(print())
		    .andExpect(status().isOk());
	}
	
	@Test
	public void testPwd() throws Exception{
		mock.perform(post("/find_pwd")
			 	.param("id", "test")
			 	.param("email", "dkcl.8642@gmail.com"))
			    .andDo(print())
			    .andExpect(status().isOk());
	}
	
	@Test
	public void testEmail() throws Exception{
		//as.sendAuthMail("dkcl.8642@gmail.com");
	}
	
	@Test
	public void testSignup() throws Exception{
		MemberDTO dto = new MemberDTO();
		dto.setId("rkskel");
		dto.setPwd("rkskel");
		dto.setName("가나디");
		dto.setEmail("a1085547549@naver.com");
		dto.setNick("가나디");
		dto.setPhone("01012345678");
		
		String jsonRequest = new ObjectMapper().writeValueAsString(dto);

	    mock.perform(post("/signup")
	            .contentType(MediaType.APPLICATION_JSON)  // JSON 요청 설정
	            .content(jsonRequest))  // 변환된 JSON 전달
	            .andDo(print())
	            .andExpect(status().isOk());
	}
	
	@Test
	public void TestIdCheck() throws Exception{
		mock.perform(post("/id_check")
			 	.param("id", "choi"))
			    .andDo(print())
			    .andExpect(status().isOk());
	}
	
	@Test
	public void TestNick() {
		as.checkNick("test");
	}
	
}
