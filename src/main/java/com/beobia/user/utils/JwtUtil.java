package com.beobia.user.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Date;
import java.util.stream.Collectors;


public class JwtUtil {

    private final Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

    public String generateAccessToken(User user, int minutes){
        String token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + minutes * 60 * 1000))
                .withIssuer("beobiaLLC")
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        return token;
    }

    public String generateRefreshToken(User user, int minutes){
        String token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + minutes * 60 * 1000))
                .withIssuer("beobiaLLC")
                .sign(algorithm);
        return token;
    }


}
