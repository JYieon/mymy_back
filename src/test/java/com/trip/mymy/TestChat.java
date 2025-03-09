package com.trip.mymy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.trip.mymy.mybatis.ChatMapper;
import com.trip.mymy.service.ChatServiceImpl;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"classpath:testChat.xml", 
		"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class TestChat {
	
	@Autowired ChatServiceImpl cs;
	@Autowired ChatMapper cm;
	
	@Test
	public void testCreateChat() {
		cs.createRoom("mymy", "mymy");
	}
	
	@Test
	public void test1ChatList() {
		cs.findChatList("mymy");
	}
	
	@Test
	public void testInvite() {
		cs.inviteMember("gkdlfn", (long)24);
	}
	
	@Test
	public void testRemoveRoom() {
		cs.removeRoom((long)24, "mymy", "방장");
	}
	
}
