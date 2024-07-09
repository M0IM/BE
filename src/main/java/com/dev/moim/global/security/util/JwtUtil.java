package com.dev.moim.global.security.util;

import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.security.principal.PrincipalDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessTokenValidityMilliseconds;
    private final Long refreshTokenValidityMilliseconds;

    public JwtUtil(
            @Value("${spring.jwt.secret}") final String secretKey,
            @Value("${spring.jwt.access-token-validity}") final Long accessTokenValidityMilliseconds,
            @Value("${spring.jwt.refresh-token-validity}") final Long refreshTokenValidityMilliseconds) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidityMilliseconds = accessTokenValidityMilliseconds;
        this.refreshTokenValidityMilliseconds = refreshTokenValidityMilliseconds;
    }

    public String createAccessToken(PrincipalDetails principalDetails) {
        return createToken(principalDetails, accessTokenValidityMilliseconds);
    }

    public String createRefreshToken(PrincipalDetails principalDetails) {
        return createToken(principalDetails, refreshTokenValidityMilliseconds);
    }

    private String createToken(PrincipalDetails principalDetails, Long validityMilliseconds) {
        ZonedDateTime issuedAt = ZonedDateTime.now();
        ZonedDateTime tokenValidity = issuedAt.plusSeconds(validityMilliseconds / 1000);

        return Jwts.builder()
                .setSubject(principalDetails.getId().toString())
                .claim("email", principalDetails.getUsername())
                .claim("role", principalDetails.getAuthorities())
                .setIssuedAt(Date.from(issuedAt.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmail(String token) {
        return getClaims(token).getBody().get("email", String.class);
    }

    public Long getRefreshTokenExpiryDate(String refreshToken) throws AuthException {
        try {
            Jws<Claims> claims = getClaims(refreshToken);
            return claims.getBody().getExpiration().getTime();
        } catch (ExpiredJwtException e) {
            throw new AuthException(AUTH_EXPIRED_TOKEN);
        } catch (SecurityException
                 | MalformedJwtException
                 | UnsupportedJwtException
                 | IllegalArgumentException e) {
            throw new AuthException(AUTH_INVALID_TOKEN);
        }
    }

    public boolean isTokenValid(String token) throws AuthException {
        try {
            Jws<Claims> claims = getClaims(token);
            Date expiredDate = claims.getBody().getExpiration();
            Date now = new Date();
            return expiredDate.after(now);
        } catch (ExpiredJwtException e) {
            throw new AuthException(AUTH_EXPIRED_TOKEN);
        } catch (SecurityException
                 | MalformedJwtException
                 | UnsupportedJwtException
                 | IllegalArgumentException e) {
            throw new AuthException(AUTH_INVALID_TOKEN);
        }
    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
    }
}
