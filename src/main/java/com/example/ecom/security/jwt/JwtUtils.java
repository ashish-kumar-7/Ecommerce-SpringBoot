package com.example.ecom.security.jwt;

import com.example.ecom.security.service.UserDetailsImp;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${spring.ecom.app.jwtCookieName}")
    private String jwtCookie;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

//    public String getJwtTokenFromHeader(HttpServletRequest request){
//        String bearerToken = request.getHeader("Authorization");
//        if(bearerToken !=null && bearerToken.startsWith("Bearer ")){
//            return bearerToken.substring(7);
//        }
//        return null;
//    }

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            System.out.println("COOKIE: " + cookie.getValue());
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImp userPrincipal) {
        String jwt = generateToken(userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60)
                .httpOnly(false)
                .build();
        return cookie;
    }

    public String generateToken(String username) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getKey(), SignatureAlgorithm.HS256).compact();

    }
//    public String generateJwtTokenFromUsername(UserDetails userDetails){
//        String username = userDetails.getUsername();
//        return Jwts.builder()
//                .setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//                .signWith(getKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }

//    public String getUsernameFromJwtToken(String bearerToken){
//        return Jwts.parser()
//                .verifyWith(getKey())
//                .build().parseSignedClaims(bearerTokentoken)
//                .getPayload().getSubject();
//    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

//    public boolean validateJwtToken(String authToken) {
//        try {
//            System.out.println("Validate");
//            Jwts.parser().verifyWith( getKey()).build().parseSignedClaims(authToken);
//            return true;
//        } catch (MalformedJwtException e) {
//            logger.error("Invalid JWT token: {}", e.getMessage());
//        } catch (ExpiredJwtException e) {
//            logger.error("JWT token is expired: {}", e.getMessage());
//        } catch (UnsupportedJwtException e) {
//            logger.error("JWT token is unsupported: {}", e.getMessage());
//        } catch (IllegalArgumentException e) {
//            logger.error("JWT claims string is empty: {}", e.getMessage());
//        }
//        return false;
//    }
public String extractUserName(String token) {
    // extract the username from jwt token
    return extractClaim(token, Claims::getSubject);
}

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
