package com.trip.mymy.dto.money;

import lombok.Setter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Setter
@Builder
public class BankServiceDTO {

	int bankSerNum;
	int roomNum;
	String member;
	String type;
	int money;
	int personalTotal;
	String date;
	int bankTotal;
}
