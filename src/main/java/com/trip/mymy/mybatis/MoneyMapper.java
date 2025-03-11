package com.trip.mymy.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.trip.mymy.dto.BankDTO;
import com.trip.mymy.dto.BankServiceDTO;
import com.trip.mymy.dto.SettlementDTO;

public interface MoneyMapper {

	public int insertMemberBank(@Param("bankNum") String bankNum, @Param("memberId") String memberId);
	public void insertSettlement(@Param("roomNum") int roomNum, @Param("toMember") String toMember, @Param("money") int money, @Param("roomMember") int roomMember);
	public void settlement(@Param("settleNum") int settleNum, @Param("id") String id);
	public void updateSettleCheck(int SettleMember, int settleNum);
	public SettlementDTO getSettlement(int roomNum);
	public int makeBank(BankDTO bank);
	public BankDTO getBank(int roomNum);
	public List<BankServiceDTO> getBankService(int roomNum);
	public int addBankService(BankServiceDTO bankSer);
}
