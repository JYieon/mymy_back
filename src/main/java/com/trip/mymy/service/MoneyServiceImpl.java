package com.trip.mymy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trip.mymy.dto.BankDTO;
import com.trip.mymy.dto.BankServiceDTO;
import com.trip.mymy.dto.SettlementDTO;
import com.trip.mymy.mybatis.MoneyMapper;

@Service
public class MoneyServiceImpl implements MoneyService {
	@Autowired MoneyMapper mm;
	
	public int insertMemberBank(String id, String bankNum) {
		int result = mm.insertMemberBank(bankNum, id);
		System.out.println(result);
		return result;
	}
	
	public void insertSettlement(int roomNum, String toMember, int money, int roomMember) {
		try {
			mm.insertSettlement(roomNum, toMember, money, roomMember);			
		} catch (Exception e) {
			System.out.println("아이디 없음");
		}
	}
	
	public void settlement(int settleNum, String id) {
		mm.settlement(settleNum, id);
	}
	
	public void updateSettleCheck(int settleMember, int settleNum) {
		mm.updateSettleCheck(settleMember, --settleNum);
	}
	
	public SettlementDTO getSettlement(int roomNum) {
		return mm.getSettlement(roomNum);
	}
	
	//모임통장
	public void makeBank(BankDTO bank) {
		mm.makeBank(bank);
	}
	
	public BankDTO getBank(int roomNum) {
		BankDTO bank = mm.getBank(roomNum);
		return bank;
	}
	
	public List<BankServiceDTO> getBankService(int roomNum) {
		List<BankServiceDTO> bankInfo = mm.getBankService(roomNum);
		return bankInfo;
	}
	
	public int addBankService(BankServiceDTO bankSer) {
		bankSer.setPersonalTotal(bankSer.getPersonalTotal() + bankSer.getMoney());
		int result = mm.addBankService(bankSer);
		return result;
	}
}
