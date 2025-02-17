package com.trip.mymy.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResDTO {
	private String name, email, image;
	private LocalDateTime regDate;

}
