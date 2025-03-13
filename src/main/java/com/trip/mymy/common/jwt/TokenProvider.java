package com.trip.mymy.common.jwt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;
import io.jsonwebtoken.*;

import org.apache.catalina.connector.InputBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.dto.token.KakaoTokenResDTO;
import com.trip.mymy.dto.token.KakaoUserInfoResDTO;
import com.trip.mymy.dto.token.TokenDTO;
import com.trip.mymy.mybatis.AuthMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.Keys;
import io.netty.handler.codec.http.HttpHeaderValues;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@Slf4j
@Component
@PropertySource("classpath:application.properties") 
public class TokenProvider {
	
	@Autowired AuthMapper am;
	
	//JWT
    private static final String AUTHORITIES_KEY = "auth"; // 권한 정보를 저장하는 키
    private static final String BEARER_TYPE = "Bearer"; // 토큰의 타입
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24L; // 24시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7L; // 7일
    private final Key key; // JWT 서명에 사용할 비밀 키
    
    //KAKAO
    @Value("${kakao.clientId}")
    private String clientId;;
    @Value("${kakao.clientSecret}")
    private String clientSecret;
    @Value("${kakao.redirectUrl}")
    private String redirectUrl;
    private static final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com/oauth/token";
    private static final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com/v2/user/me";
    
    
    // 비밀키를 기반으로 키 객체 초기화
    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
    	byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성 메서드
    public TokenDTO generateTokenDto(Authentication authentication) {
        // 인증 객체에서 권한 정보 추출
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime(); // 현재 시간
        //토큰 만료 시간 설정
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        System.out.println("Authentication Name: " + authentication.getName());

        
        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName()) // 사용자명 설정
                .claim(AUTHORITIES_KEY, "ROLE_USER")  // 권한 정보 저장
                .setExpiration(accessTokenExpiresIn)  // 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS512) // 서명 방식 설정
                .compact();
        
        //refresh Token 생성
        String refreshToken = io.jsonwebtoken.Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        
        System.out.println("Generated Token: {}" + accessToken);

        // 결과를 DTO로 반환
        return TokenDTO.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(refreshTokenExpiresIn.getTime())
                .build();
    }

    // 토큰에서 인증 객체 생성
    public Authentication getAuthentication(String accessToken) {
    	System.out.println("토큰 복호화");
    	System.out.println(accessToken);
        Claims claims = parseClaims(accessToken); // 토큰 복호화
        System.out.println(claims);
        // 토큰 복호화에 실패하면
        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        String id = claims.getSubject(); // 사용자 ID 추출

        // ID로 사용자 정보 조회 (예: MemberDTO)
        MemberDTO member = am.getUser(id);

        // 권한 정보 추출 (콤마로 구분된 권한들 처리)
        String authoritiesString = claims.get(AUTHORITIES_KEY).toString();
        if (authoritiesString == null || authoritiesString.isEmpty()) {
            throw new RuntimeException("권한 정보가 비어 있습니다.");
        }

        // 권한이 여러 개일 경우 (콤마로 구분된 권한 처리)
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(authoritiesString.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 인증 객체 생성 후 반환
        return new UsernamePasswordAuthenticationToken(member, accessToken, authorities);
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
    
    //비밀번호 재설정용 일회성 토큰
    public String generateResetToken(String id) {
    	long now = (new Date()).getTime();
        Date resetTokenExpiresIn = new Date(now + (1000 * 60 * 10)); // 10분 후 만료

        return Jwts.builder()
                .setSubject(id)  // 이메일을 토큰의 subject로 설정
                .setExpiration(resetTokenExpiresIn) // 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS512) // 서명 방식 설정
                .compact();
    }
    
    //카카오
    public String getKakaoAccessToken(String code) throws Exception {
    	String accessToken = "";
    	String refreshToken = "";
    	
    	try {
    		URL url = new URL(KAUTH_TOKEN_URL_HOST);
    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    		
    		conn.setRequestMethod("POST");
    		conn.setDoOutput(true);
    		
    		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
    		StringBuilder sb = new StringBuilder();
    		sb.append("grant_type=authorization_code");
    		sb.append("&client_id=" + clientId);
    		sb.append("&redirect_uri=" + redirectUrl);
    		sb.append("&code=" + code);
    		bw.write(sb.toString());
    		bw.flush();
    		
    		int responseCode = conn.getResponseCode();
    		System.out.println("resCode : " + responseCode);
    		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    		String line = "";
    		String result = "";
    		
    		while((line = br.readLine()) != null) {
    			result += line;
    		}
    		System.out.println("response body : " + result);
    		
    		//Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
    		JsonElement element = JsonParser.parseString(result);
    		
    		accessToken = element.getAsJsonObject().get("access_token").getAsString();
    		refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();
    		
    		System.out.println("access_token : " + accessToken);
    		System.out.println("refresh_token : " + refreshToken);
    		
    		br.close();
    		bw.close();
    		
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return accessToken;
    }
    
    public HashMap<String, Object> getUserKakaoInfo(String accessToken) throws Exception{
    	System.out.println("*****accessToken" + accessToken);
    	HashMap<String, Object> userInfo = new HashMap<String, Object>();
    	
    	try {
    		URL url = new URL(KAUTH_USER_URL_HOST);
    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    		conn.setRequestMethod("GET");
    		
    		conn.setRequestProperty("Authorization", "Bearer " + accessToken);
    		
    		int responseCode = conn.getResponseCode();
    		System.out.println("responseCode : " + responseCode);
    		
    		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    		
    		String line = "";
    		String result = "";
    		
    		while((line = br.readLine()) != null) {
    			result += line;
    		}
    		System.out.println("response body : " + result);
    		
    		JsonElement element = JsonParser.parseString(result);
    		
    		String id = element.getAsJsonObject().get("id").getAsString();
    		
    		JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
    		JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
    		
    		String nickname = properties.getAsJsonObject().get("nickname").getAsString();
    		if(kakaoAccount.getAsJsonObject().get("email") != null) {
    			String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
    			userInfo.put("email", email);
    		}
    		
    		userInfo.put("nickname", nickname);
    		userInfo.put("id", id);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	return userInfo;
    }
    
}
