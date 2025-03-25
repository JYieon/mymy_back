package com.trip.mymy.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.trip.mymy.dto.money.BankDTO;
import com.trip.mymy.dto.money.BankServiceDTO;
import com.trip.mymy.dto.money.SettlementDTO;
import com.trip.mymy.dto.money.SettlementServiceDTO;
import com.trip.mymy.mybatis.MoneyMapper;

@Service
public class MoneyServiceImpl implements MoneyService {
	@Autowired MoneyMapper mm;
	
	public int insertMemberBank(String id, String bankNum) {
		int result = mm.insertMemberBank(bankNum, id);
		System.out.println(result);
		return result;
	}
	
	public int insertSettlement(int roomNum, String toMember, int money, int roomMember) {
		int result = mm.insertSettlement(roomNum, toMember, money, roomMember);			
		return result;
	}
	
	public void settlement(int settleNum, String id) {
		mm.settlement(settleNum, id);
	}
	
	public void updateSettleCheck(int settleMember, int settleNum) {
		mm.updateSettleCheck(--settleMember, settleNum);
	}
	
	public List<SettlementDTO> getSettlement(int roomNum) {
		List<SettlementDTO> list = mm.getSettlement(roomNum);
		return list;
	}
	
	public List<SettlementServiceDTO> getSettlementService(int settleNum){
		List<SettlementServiceDTO> list = mm.getSettlementService(settleNum);
		return list;
	}
	
	//모임통장
	public void makeBank(BankDTO bank) {
	    Random random = new Random();
	    String formattedBankNumber;
	    String bankNum;
	    
	    // 유효한 은행 번호가 나올 때까지 반복
	    do {
	        long bankNumber = (long)(random.nextDouble() * 9000000000L) + 1000000000L;

	        formattedBankNumber = String.format("%03d-%04d-%03d", 
	                bankNumber / 10000000,               // 첫 번째 세 자리
	                (bankNumber / 10000) % 10000,       // 두 번째 네 자리
	                bankNumber % 10000);                // 세 번째 세 자리

	        // 통장번호 유효성 검사
	        bankNum = mm.checkBankNum(formattedBankNumber);

	    } while (bankNum != null);  // 이미 존재하는 번호가 있으면 계속 반복

	    // 유효한 bankNum을 찾으면 setBankNum 및 makeBank 호출
	    bank.setBankNum(formattedBankNumber);
	    mm.makeBank(bank);
	}
	
	public BankDTO getBank(int roomNum) {
		BankDTO bank = mm.getBank(roomNum);
		return bank;
	}
	
	public List<BankServiceDTO> getBankService(String bankNum) {
		List<BankServiceDTO> bankInfo = mm.getBankService(bankNum);
		return bankInfo;
	}
	
	public int addBankService(BankServiceDTO bankSer, BankDTO bank) {
		int result = 0;
		Integer personalTotal = mm.getPersonalTotal(bankSer.getMember());
		personalTotal = (personalTotal != null) ? personalTotal : 0;
		
		if(bankSer.getType().equals("+")) {
			
			bankSer.setPersonalTotal(personalTotal + bankSer.getMoney());			
			bankSer.setBankTotal(bank.getTotal() + bankSer.getMoney());
			try {
				result = mm.addBankService(bankSer);
				bank.setTotal(bank.getTotal() + bankSer.getMoney());
				if(bank.getTotal() >= bank.getTarget()) {
					bank.setCheck("y");
				}
				result += mm.updateBank(bank);
			}catch(Exception e){
				System.out.println(e);
			}
		}else {
			try {
				bankSer.setPersonalTotal(personalTotal);
				bankSer.setBankTotal(bank.getTotal() - bankSer.getMoney());
				result = mm.addBankService(bankSer);
				bank.setTotal(bank.getTotal() - bankSer.getMoney());
				result += mm.updateBank(bank);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return result;
	}
}
