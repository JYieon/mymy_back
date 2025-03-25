package com.trip.mymy.dto.money;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BankDTO {
	String bankNum;
	int roomNum;
	String bankName;
	int total;
	int target;
	String check;
}
