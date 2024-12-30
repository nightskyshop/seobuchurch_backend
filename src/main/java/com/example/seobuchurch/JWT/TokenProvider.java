package com.example.seobuchurch.JWT;

import com.example.seobuchurch.DTO.TokenDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 3; // 3일

    private final Key key;
    private final Key refreshKey;
    public TokenProvider(@Value("${jwt.secret}") String secretKey, @Value("${jwt.refresh.secret}") String refreshSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshSecretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
    }

    public TokenDTO generateTokenDto(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date tokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        System.out.println("토큰만료시간: " + tokenExpiresIn);

        Integer subject;
        subject = Integer.valueOf(authentication.getName());

        String subject2 = String.valueOf(subject);

        System.out.println("authorities: " + authorities);

        String accessToken = Jwts.builder()
                .setSubject(subject2)
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(tokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        String refreshToken = Jwts.builder()
                .setSubject(subject2)
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(refreshTokenExpiresIn)
                .signWith(refreshKey, SignatureAlgorithm.HS512)
                .compact();


        return TokenDTO.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .tokenExpiresIn(tokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(refreshTokenExpiresIn.getTime())
                .build();
    }

    public TokenDTO getAccessTokenFromRefreshToken(String refreshToken) {
        boolean isValid = validateRefreshToken(refreshToken);

        if (isValid) {
            String authorities = getAuthoritiesFromRefreshToken(refreshToken);
            String subject = getUserIdFromRefreshToken(refreshToken);

            long now = (new Date()).getTime();
            Date tokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
            System.out.println("토큰만료시간: " + tokenExpiresIn);

            String accessToken = Jwts.builder()
                    .setSubject(subject)
                    .claim(AUTHORITIES_KEY, authorities)
                    .setExpiration(tokenExpiresIn)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();

            System.out.println("accessToken: " + accessToken);
            System.out.println("authorities: " + authorities);

            return TokenDTO.builder()
                    .grantType(BEARER_TYPE)
                    .accessToken(accessToken)
                    .tokenExpiresIn(tokenExpiresIn.getTime())
                    .build();
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "accessToken 획득에 실패했습니다.");
    }


    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"만료된 JWT access 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            System.out.println("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            System.out.println("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getUserIdFromRefreshToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getAuthoritiesFromRefreshToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(token).getBody();
        return claims.get(AUTHORITIES_KEY, String.class);
    }
}
