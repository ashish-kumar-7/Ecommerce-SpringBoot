package com.example.ecom.security.jwt;

import com.example.ecom.security.service.UserDetailsServiceImp;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsServiceImp userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());

//        String authHeader = request.getHeader("Authorization");
        String token = jwtUtils.getJwtFromCookies(request);
        String userName=null;

        if(token != null){
            userName = jwtUtils.extractUserName(token);
        }

        logger.debug("AuthTokenFilter.java: {}", token);

        if(userName != null && SecurityContextHolder.getContext().getAuthentication()==null){

            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

            logger.debug("Roles from JWT: {}", userDetails.getAuthorities());

            if(jwtUtils.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
//        try{
//            String jwtToken = jwtUtils.getJwtTokenFromHeader(request);
//            logger.debug("AuthTokenFilter.java: {}", jwtToken);
//            if(jwtToken!=null && jwtUtils.validateJwtToken(jwtToken)){
//                String username= jwtUtils.getUsernameFromJwtToken(jwtToken);
//
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
//                logger.debug("Roles from JWT: {}", userDetails.getAuthorities());
//
//                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            }
//        } catch (Exception e) {
//            logger.error("Cannot set user authentication: {}", e);
//        }
//        filterChain.doFilter(request,response);
//    }
}
