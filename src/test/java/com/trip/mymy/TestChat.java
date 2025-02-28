package com.trip.mymy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.trip.mymy.service.ChatServiceImpl;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"classpath:testAuth.xml", 
		"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class TestChat {
	
	@Autowired ChatServiceImpl cs;
	
	@Test
	public void testCreateChat() {
		cs.createRoom("test", "테스트");
	}
}
