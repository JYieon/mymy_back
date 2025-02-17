package com.trip.mymy.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class JwtDecoder {
    private static final String SECRET_KEY = "KJGHIUYTRGHJKLYTRVMYMYJKLPOIUYTREWQASDFGMYMYQWERTYUIOPMNBVCXZASDF";

    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb20udHJpcC5teW15LmR0by5NZW1iZXJEVE9ANzA2ZjZkMjYiLCJhdXRoIjoiIiwiZXhwIjoxNzM5Nzc2ODcyfQ.UJTab2JQnHcaOC0UFCCQkSLT2jNL610pkdXuGnaipnOBBU6RnecKgPf_pkrV5z961PjrRXS50ttZj01FTk0OGA";
        
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes()) 
                .parseClaimsJws(token)
                .getBody();
            
            System.out.println("✅ 토큰 내용:");
            System.out.println("Subject: " + claims.getSubject());  // 사용자 정보
            System.out.println("Auth: " + claims.get("auth"));     // 권한 정보
            System.out.println("Exp: " + claims.getExpiration()); // 만료 시간
        } catch (SignatureException e) {
            System.out.println("❌ 토큰 검증 실패: 서명 불일치");
        }
    }
}

