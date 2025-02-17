package com.trip.mymy.common.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import io.jsonwebtoken.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.dto.TokenDTO;
import com.trip.mymy.mybatis.AuthMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class TokenProvider {
	
	@Autowired AuthMapper am;
	
    private static final String AUTHORITIES_KEY = "auth"; // 권한 정보를 저장하는 키
    private final Key key; // JWT 서명에 사용할 비밀 키

    // 비밀키를 기반으로 키 객체 초기화
    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // 토큰 생성 메서드
    public TokenDTO generateTokenDto(Authentication authentication) {
        // 인증 객체에서 권한 정보 추출
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 현재 시간과 토큰 만료 시간 계산
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + 30 * 60 * 1000); // 30분

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName()) // 사용자명 설정
                .claim(AUTHORITIES_KEY, authorities)  // 권한 정보 저장
                .setExpiration(accessTokenExpiresIn)  // 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS512) // 서명 방식 설정
                .compact();
        
        System.out.println("Generated Token: {}" + accessToken);

        // 결과를 DTO로 반환
        return TokenDTO.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .tokenExpiresIn(accessTokenExpiresIn.getTime())
                .build();
    }

    // 토큰에서 인증 객체 생성
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String id = claims.getSubject();
        
        MemberDTO member = am.getUser(id);

        // 권한 정보 추출
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 인증 객체 생성 후 반환
        return new UsernamePasswordAuthenticationToken(member, token, authorities);
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.info("JWT 검증 실패: {}", e.getMessage());
        }
        return false;
    }

    // 토큰의 Claims(내용) 추출
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
