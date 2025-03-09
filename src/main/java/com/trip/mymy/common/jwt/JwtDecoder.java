package com.trip.mymy.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class JwtDecoder {
    private static final String SECRET_KEY = "Y29tcGxleCBkYXRhIGZvciBzZWN1cml0eSBhbmQgaGFzaC11c2Uga2V5IGNvbmZpZ3VyZWQgaW4gdGhlIGhlYWRlZCBtZWRpYSBvZiBqb3VybmFsIHJlY29nbml0aW9uLg";

    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0IiwiYXV0aCI6IiIsImV4cCI6MTc0MTE2ODUzNH0.TtDgCQa3-eQkDlrwUfv95TTQZN9yiaMfVDLzQnNUG-tHHz1qzFPYomdEqVB0l6OJ55qqmV1Ao9hKwE57ODgDJA";
        
        try {
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes()) // 서명에 사용할 키 설정
                    .build();

            // 토큰 복호화 (서명 검증 포함)
            Claims claims = parser.parseClaimsJws(token).getBody();
            
            System.out.println("✅ 토큰 내용:");
            System.out.println("Subject: " + claims.getSubject());  // 사용자 정보
            System.out.println("Auth: " + claims.get("auth"));     // 권한 정보
            System.out.println("Exp: " + claims.getExpiration()); // 만료 시간
        } catch (SignatureException e) {
            System.out.println("❌ 토큰 검증 실패: 서명 불일치");
        }
    }
}
