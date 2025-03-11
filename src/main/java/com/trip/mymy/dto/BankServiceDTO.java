package com.trip.mymy.dto;

import lombok.Setter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Setter
@Builder
public class BankServiceDTO {

	int bankSerNum;
	int roomNum;
	String type;
	int money;
	String member;
	int personalTotal;
	String date;
}
