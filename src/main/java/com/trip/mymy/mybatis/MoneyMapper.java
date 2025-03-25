package com.trip.mymy.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.trip.mymy.dto.money.BankDTO;
import com.trip.mymy.dto.money.BankServiceDTO;
import com.trip.mymy.dto.money.SettlementDTO;
import com.trip.mymy.dto.money.SettlementServiceDTO;

public interface MoneyMapper {

	public int insertMemberBank(@Param("bankNum") String bankNum, @Param("memberId") String memberId);
	public int insertSettlement(@Param("roomNum") int roomNum, @Param("toMember") String toMember, @Param("money") int money, @Param("roomMember") int roomMember);
	public void settlement(@Param("settleNum") int settleNum, @Param("id") String id);
	public void updateSettleCheck(@Param("settleMember") int settleMember, @Param("settleNum") int settleNum);
	public List<SettlementDTO> getSettlement(int roomNum);
	public List<SettlementServiceDTO> getSettlementService(int settleNum);
	public int makeBank(BankDTO bank);
	public BankDTO getBank(int roomNum);
	public List<BankServiceDTO> getBankService(String bankNum);
	public int addBankService(BankServiceDTO bankSer);
	public String checkBankNum(String bankNum);
	public int updateBank(BankDTO bank);
	public Integer getPersonalTotal(String id);
}
