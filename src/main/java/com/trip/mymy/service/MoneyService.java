package com.trip.mymy.service;

import java.util.List;

import com.trip.mymy.dto.BankDTO;
import com.trip.mymy.dto.BankServiceDTO;
import com.trip.mymy.dto.SettlementDTO;

public interface MoneyService {
	public int insertMemberBank(String id, String bankNum);
	public void insertSettlement(int roomNum, String toMember, int money, int roomMember);
	public void settlement(int settleNum, String id);
	public void updateSettleCheck(int settleMember, int settleNum);
	public SettlementDTO getSettlement(int roomNum);
	public void makeBank(BankDTO bank);
	public BankDTO getBank(int roomNum);
	public List<BankServiceDTO> getBankService(int roomNum);
	public int addBankService(BankServiceDTO bankSer);
}
