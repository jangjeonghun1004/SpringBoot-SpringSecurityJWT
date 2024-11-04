package com.example.demo.config.security;

import com.example.demo.service.UserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;


@Component
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;
    private final long tokenValidMillisecond = 1000L * 60 * 60; // 1시간
    private final String secretKey = "SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"; // 32 bit 문자열
    private final Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(
            Base64.getEncoder().encodeToString(this.secretKey.getBytes(StandardCharsets.UTF_8))
    ));


    @Autowired
    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 사용자 아이디, 사용자 권한을 포함한 인증 토큰을 생성합니다.
     *
     * @param userID 사용자 아이디
     * @param roles 사용자 권한
     * @return String JWT 인증 토큰
     */
    public String createToken(String userID, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userID);
        claims.put("roles", roles);

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + this.tokenValidMillisecond))
                .signWith(this.key, SignatureAlgorithm.HS256).compact();
    }

    /**
     * 사용자 자격증명을 생성합니다.
     *
     * @param token JWT 인증 토큰
     * @return Authentication 사용자 자격증명
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.getByUid(this.getUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    /**
     * JWT 인증 토큰에서 사용자 아이디를 추출합니다.
     *
     * @param token JWT 인증 토큰
     * @return String 사용자 아이디
     */
    public String getUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * 헤더 정보에 "X-AUTH-TOKEN" 인증 토큰이 존재하는지 확인합니다.
     *
     * @param httpServletRequest HttpServletRequest 객체
     * @return String "X-AUTH-TOKEN" 인증 토큰
     */
    public String resolveToken(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("X-AUTH-TOKEN");
    }

    /**
     * "X-AUTH-TOKEN" 인증 토큰의 유효성을 확인합니다.
     *
     * @param token "X-AUTH-TOKEN" 인증 토큰
     * @return boolean "X-AUTH-TOKEN" 인증 토큰이 유효하다면 true 그렇치 않다면 false
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(this.key)
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}