package com.trip.mymy.dto.money;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettlementDTO {
	int settleNum;
	int roomNum;
	String toMember;
	int money;
	String check;
}
