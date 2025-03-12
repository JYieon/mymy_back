package com.trip.mymy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.trip.mymy.dto.money.BankDTO;
import com.trip.mymy.dto.money.BankServiceDTO;
import com.trip.mymy.mybatis.ChatMapper;
import com.trip.mymy.mybatis.MoneyMapper;
import com.trip.mymy.service.ChatServiceImpl;
import com.trip.mymy.service.MoneyServiceImpl;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"classpath:testChat.xml", 
		"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class TestChat {
	
	@Autowired ChatServiceImpl cs;
	@Autowired ChatMapper cm;
	@Autowired MoneyMapper mm;
	@Autowired MoneyServiceImpl ms;
	
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
	
	@Test
	public void testBank() {
		int result = mm.insertMemberBank("456789153", "test");
		System.out.println(result);
	}
	
	@Test
	public void testSettlementAdd() {
		ms.insertSettlement(25, "test", 2000, 2);
	}
	
	@Test
	public void testSettlement() {
		ms.settlement(7, "mymy");
	}
	
	@Test
	public void testSettlementList() {
		ms.getSettlement(25);
	}
	
	@Test
	public void testGetBank() {
		ms.getBank(25);
	}
	
	@Test
	public void testMakeBank() {
		BankDTO bank = BankDTO.builder()
				.roomNum(25)
				.bankName("여행가자")
				.target(300000)
				.build();
		ms.makeBank(bank);
	}
	
	@Test
	public void testGetBankSer() {
		ms.getBankService(25);
	}
	
	@Test
	public void testBankSer() {
		BankServiceDTO bankSer = BankServiceDTO.builder()
				.roomNum(25)
				.type("+")
				.money(100000)
				.member("test")
				.personalTotal(0)
				.build();
		ms.addBankService(bankSer);
	}
	
}
