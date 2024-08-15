package com.dev.moim.global.security.util;

import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.security.principal.PrincipalDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@Component
@Getter
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessTokenValiditySec;
    private final Long refreshTokenValiditySec;
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public JwtUtil(
            @Value("${spring.jwt.secret}") final String secretKey,
            @Value("${spring.jwt.access-token-validity}") final Long accessTokenValiditySec,
            @Value("${spring.jwt.refresh-token-validity}") final Long refreshTokenValiditySec) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValiditySec = accessTokenValiditySec;
        this.refreshTokenValiditySec = refreshTokenValiditySec;
    }

    public String createAccessToken(PrincipalDetails principalDetails) {
        return createToken(principalDetails, accessTokenValiditySec);
    }

    public String createRefreshToken(PrincipalDetails principalDetails) {
        return createToken(principalDetails, refreshTokenValiditySec);
    }

    private String createToken(
            PrincipalDetails principalDetails, Long validitySeconds) {
        Instant issuedAt = Instant.now();
        Instant expirationTime = issuedAt.plusSeconds(validitySeconds);

        return Jwts.builder()
                .setSubject(String.valueOf(principalDetails.getUserId()))
                .claim("role", principalDetails.getAuthorities())
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserId(String token) {
        return getClaims(token).getBody().getSubject();
    }

    public Long getExpiration(String token) {
        return getClaims(token).getBody().getExpiration().getTime();
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

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication (String token) {
        Claims claims = getClaims(token).getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
