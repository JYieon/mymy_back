package com.trip.mymy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BankDTO {
	int roomNum;
	String bankName;
	int total;
	int target;
	String check;
}
