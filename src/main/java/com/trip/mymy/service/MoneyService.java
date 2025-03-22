package com.trip.mymy.service;

import java.util.List;

import com.trip.mymy.dto.money.BankDTO;
import com.trip.mymy.dto.money.BankServiceDTO;
import com.trip.mymy.dto.money.SettlementDTO;
import com.trip.mymy.dto.money.SettlementServiceDTO;

public interface MoneyService {
	public int insertMemberBank(String id, String bankNum);
	public int insertSettlement(int roomNum, String toMember, int money, int roomMember);
	public void settlement(int settleNum, String id);
	public void updateSettleCheck(int settleMember, int settleNum);
	public List<SettlementDTO> getSettlement(int roomNum);
	public List<SettlementServiceDTO> getSettlementService(int settleNum);
	public void makeBank(BankDTO bank);
	public BankDTO getBank(int roomNum);
	public List<BankServiceDTO> getBankService(int roomNum);
	public int addBankService(BankServiceDTO bankSer);
}
