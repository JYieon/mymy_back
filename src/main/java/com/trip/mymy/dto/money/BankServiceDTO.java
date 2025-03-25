package com.trip.mymy.dto.money;

import lombok.Setter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Setter
@Builder
public class BankServiceDTO {
	int bankSerNum;
	String bankNum;
	String member;
	String type;
	int money;
	int personalTotal;
	String date;
	int bankTotal;
}
