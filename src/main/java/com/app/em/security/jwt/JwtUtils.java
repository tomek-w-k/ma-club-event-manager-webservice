package com.app.em.security.jwt;

import com.app.em.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class JwtUtils
{
    private static Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${em.app.tokenSecret}")
    private String tokenSecret;

    @Value("${em.app.tokenExpiration}")
    private int tokenExpiration;


    public String generateAccessToken(Authentication authentication)
    {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date( new Date().getTime() + tokenExpiration ))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    public String getEmailFromAccessToken(String token)
    {
        return Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateAccessToken(String token)
    {
        try
        {
            Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token);
            return true;
        }
        catch(SignatureException e) { logger.error("Invalid access token signature : {}", e.getMessage()); }
        catch(MalformedJwtException e) { logger.error("Invalid access token: {}", e.getMessage()); }
        catch(ExpiredJwtException e) { logger.error("Access token is expired: {}", e.getMessage()); }
        catch(UnsupportedJwtException e) { logger.error("Access token is unsupported: {}", e.getMessage()); }
        catch(IllegalArgumentException e) { logger.error("Jwt claims string is empty: {}", e.getMessage()); }

        return false;
    }
}
