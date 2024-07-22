package com.dev.moim.global.security.filter;

import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.redis.service.LogoutAccessTokenService;
import com.dev.moim.global.security.principal.PrincipalDetailsService;
import com.dev.moim.global.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final PrincipalDetailsService principalDetailsService;
    private final LogoutAccessTokenService logoutAccessTokenService;

    @Override
    public void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String accessToken = getJwtFromRequest(request);

        log.info("** JwtFilter **");

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (logoutAccessTokenService.isTokenExist(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        if(jwtUtil.isTokenValid(accessToken)) {
            String email = jwtUtil.getEmail(accessToken);
            UserDetails userDetails = principalDetailsService.loadUserByUsername(email);

            if (userDetails != null) {
                setAuthenticationToContext(userDetails);
            } else {
                throw new AuthException(ErrorStatus.USER_NOT_FOUND);
            }
        } else {
            throw new AuthException(ErrorStatus.AUTH_INVALID_TOKEN);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) ? bearerToken.replace("Bearer ", ""): null;
    }

    private void setAuthenticationToContext(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, "", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
