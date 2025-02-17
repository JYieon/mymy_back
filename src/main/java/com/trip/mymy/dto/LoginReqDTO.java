package com.trip.mymy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginReqDTO {
	private String id, pwd;
	
	public LoginReqDTO() {
		
	}
	
	public LoginReqDTO(String id, String pwd) {
		this.id = id;
		this.pwd = pwd;
	}

}
