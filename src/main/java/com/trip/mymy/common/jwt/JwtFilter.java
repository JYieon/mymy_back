package com.trip.mymy.common.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    @Value("${jwt.secret}")
    private String secretKey;
    
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        
//        String token = request.getHeader("Authorization");
//
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7); // "Bearer " 제거
//            
//            try {
//                Claims claims = Jwts.parserBuilder()
//                        .setSigningKey(secretKey) // 비밀키 설정
//                        .parseClaimsJws(token)
//                        .getBody();
//
//                Date expiration = claims.getExpiration();
//                if (expiration.before(new Date())) {
//                    throw new ExpiredJwtException(null, claims, "토큰이 만료됨");
//                }
//
//                // 정상적인 토큰이면 Security Context에 저장
//                Authentication authentication = getAuthentication(claims);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            } catch (ExpiredJwtException e) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("토큰이 만료되었습니다.");
//                return;
//            }
//        }
//        
//        filterChain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        String jwt = resolveToken(request); // 헤더에서 JWT 추출
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 객체 설정
        }
        filterChain.doFilter(request, response); // 다음 필터로 요청 전달
    }

    // Authorization 헤더에서 토큰 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 부분 제거
        }
        return null;
    }
}
