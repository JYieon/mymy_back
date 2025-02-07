package com.trip.mymy;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import com.trip.mymy.controller.AuthController;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"classpath:testLogin.xml", 
		"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class TestAuth {
	
	@Autowired AuthController lc;
	MockMvc mock;
	
	@Before //org.junit.before
	public void setUp() {
		System.out.println("--- setUp 실행 ---");
		mock = MockMvcBuilders.standaloneSetup(lc).build();
	}
	
	@Test
	public void testLc() throws Exception{
		Map<String, String> loginData = new HashMap<String, String>();
		loginData.put("id", "test");
		loginData.put("pwd", "choi123");
		
		lc.loginCheck(loginData);
		
//		mock.perform(post("/login")
//				.param("id", loginData.get("id"))
//				.param("pwd", loginData.get("pwd")))
//				.andDo(print())
//				.andExpect(status().isBadRequest());  // 400 상태 코드 확인              
	}
}
