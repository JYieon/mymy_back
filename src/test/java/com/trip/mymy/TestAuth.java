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
		
		 mock.perform(post("/login")
				 .contentType(MediaType.APPLICATION_JSON)
				    .content(jsonRequest))
				    .andDo(print())
				    .andExpect(status().isOk());
	}
	
	@Test
	public void Testsignup(){
		MemberDTO dto = new MemberDTO();
		dto.setId("test");
		dto.setPwd("test123");
		dto.setName("테스트");
		dto.setEmail("dkcl.8642@gmail.com");
		dto.setNick("테스트");
		dto.setPhone("01012345678");
		
		int result = am.insertUser(dto);
		
		System.out.println("result : " + result);
		assertEquals(1, result);
	}
}
