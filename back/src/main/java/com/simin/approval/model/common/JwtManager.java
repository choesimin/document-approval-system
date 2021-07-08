package com.simin.approval.model.common;

import com.simin.approval.exception.TokenExpiredException;
import com.simin.approval.model.dto.User;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtManager {
    private final String security_key = "This_Is_A_Security_Key";
    //private final Long expired_time = 1000 * 60L * 60L;
    private final Long expired_time = 1000 * 10L;    // token expire test

    public String generateJwtToken(User user) {
        Date date = new Date();
        return Jwts.builder()
                .setSubject(user.getId())
                .setHeader(createHeader())
                .setClaims(createClaims(user))
                .setExpiration(new Date(date.getTime() + expired_time))
                .signWith(SignatureAlgorithm.HS256, security_key)
                .compact();
    }

    public int getSeqFromToken(String token) {
        return (int) getClaims(token).get("seq");
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regdate", System.currentTimeMillis());
        return header;
    }

    private Map<String, Object> createClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("seq", user.getSeq());
        return claims;
    }

    private Claims getClaims(String token) {
        checkToken(token);

        return Jwts.parser().setSigningKey(security_key).parseClaimsJws(token).getBody();
    }

    private void checkToken(String token) {
        try {
            // check whether token is null or not
            if (token.equals("null")) {
                throw new TokenExpiredException();
            }

            // check whether token is expired or not
            Jwts.parser().setSigningKey(security_key).parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        }
    }
}
