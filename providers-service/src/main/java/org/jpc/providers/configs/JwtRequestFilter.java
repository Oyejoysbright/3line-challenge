package org.jpc.providers.configs;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jpc.providers.services.SerJwtUserDetails;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final SerJwtUserDetails jwtUserDetailsService;

    private final JwtTokenUtil jwtTokenUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");
        
        String jwtToken = null;
        String username = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException | BadCredentialsException e) {
                logger.error("::::: IllegalArgumentException or BadCredentialsException ==>"+e.getLocalizedMessage());
                request.setAttribute("exception", "Bad credentials - Could not process token");
            } catch (ExpiredJwtException e) {
                logger.warn("::::: JWT Token has expired ==>" + e.getMessage());
                String isRefreshToken = request.getHeader("x-refresh-token");
                String requestURL = request.getRequestURL().toString();
                if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refresh/token")) {
                    allowForRefreshToken(e, request);
                } else {
                    logger.error("::::: Access Token has expired ==>"+e.getMessage());
                    request.setAttribute("exception", "Expired Token - Access Token has expired");
                }
            } catch (Exception e) {
                logger.error("::::: Unable to get JWT Token ==>"+e.getMessage());
                request.setAttribute("exception", "Could not process token "+e.getMessage());
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
            request.setAttribute("exception", "Bearer Authorization header is required");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                jwtTokenUtil.setUserToken(jwtToken);
            }
        }
        chain.doFilter(request, response);
    }
    
    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        null, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        request.setAttribute("claims", ex.getClaims());
    }

}
