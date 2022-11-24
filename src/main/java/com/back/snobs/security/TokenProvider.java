package com.back.snobs.security;

import com.back.snobs.config.AuthProperties;
import com.back.snobs.domain.snob.Role;
import com.back.snobs.domain.snob.Snob;
import com.back.snobs.domain.snob.SnobRepository;
import com.back.snobs.service.RefreshTokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenProvider {
    private final AuthProperties authProperties;
    private Key key;
    private final SnobRepository snobRepository;
    private final RefreshTokenService refreshTokenService;

    @PostConstruct
    public void initKey() {
        this.key = Keys.hmacShaKeyFor(authProperties.getAuth().getTokenSecret().getBytes(StandardCharsets.UTF_8));
    }

    // create token
    public String createAccessToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return createAccessToken(userPrincipal.getEmail());
    }

    public String createAccessToken(String userEmail) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + authProperties.getAuth().getTokenExpirationMsec());

        return Jwts.builder()
                .setSubject(userEmail)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
//                .signWith(SignatureAlgorithm.HS512, authProperties.getAuth().getTokenSecret())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + authProperties.getAuth().getRefreshTokenExpirationMsec());

        String refreshToken = Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
//                .signWith(SignatureAlgorithm.HS512, authProperties.getAuth().getTokenSecret())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        refreshTokenService.saveToken(refreshToken, userPrincipal.getEmail());
        return refreshToken;
    }

    public String getUserEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken, Role role) {
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken);

            return isValidRoleUser(jws.getBody().get("sub", String.class), role);
        } catch (SecurityException ex) {
            System.out.println("Invalid JWT signature");
            throw ex;
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
            throw ex;
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
            return false;
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
            throw ex;
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
            throw ex;
        }
    }

    private boolean isValidRoleUser(String userEmail, Role role) {
        Snob snob = snobRepository.findByUserEmail(userEmail).orElseThrow(() ->
                new UsernameNotFoundException("User not found with email :" + userEmail));

        return Role.roleCheck(snob.getRole(), role);
    }
}
