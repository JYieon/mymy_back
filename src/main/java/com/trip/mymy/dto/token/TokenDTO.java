package com.trip.mymy.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
	private String grantType; // �씤利� 諛⑹떇
    private String accessToken; // �븸�꽭�뒪 �넗�겙
    private String refreshToken; // 由ы봽�젅�떆 �넗�겙
    private Long accessTokenExpiresIn; // �븸�꽭�뒪 �넗�겙 留뚮즺 �떆媛�
    private Long refreshTokenExpiresIn; // 由ы봽�젅�떆 �넗�겙 留뚮즺 �떆媛�
}
