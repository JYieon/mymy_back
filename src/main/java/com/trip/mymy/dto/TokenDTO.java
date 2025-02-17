package com.trip.mymy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
	private String grantType, accessToken;
	private Long tokenExpiresIn;
}
