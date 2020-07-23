package com.kakaopay.coupon.api.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Component
public class JwtProvider {
    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${jwt.secret-key.authentication}")
    private String secretKey;
    public static final long MAX_TOKEN_VALID_MILLISECONDS = 60 * 60 * 1000L;  // 1 hour

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(Long no, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(Long.toString(no));
        claims.put("roles", roles);
        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Date expiredDate = new Date(now.getTime() + MAX_TOKEN_VALID_MILLISECONDS);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("kakaopay.com")
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getJwtSubject(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getJwtSubject(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        return authorization.split(" ")[1];
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
            return !claimsJws.getBody()
                    .getExpiration()
                    .before(now);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
