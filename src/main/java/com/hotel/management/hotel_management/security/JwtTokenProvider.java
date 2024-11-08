package com.hotel.management.hotel_management.security;

import com.hotel.management.hotel_management.exception.APIException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

/**
 * This class provides functionalities for generating, parsing, and validating JWT tokens.
 * It ensures secure token management, which includes encryption and handling expiration.
 */
@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app-jwt-expiration-milliseconds}")
    private Long jwtExpirationDate;

    // generate JWT token
    public String generateToken(Authentication authentication)
    {
        String username = authentication.getName();

        Date currentDate = new Date();

        Date expiredDate = new Date(currentDate.getTime() +  jwtExpirationDate);

        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expiredDate)
                .signWith(key())
                .compact();

        return token;
    }

    private Key key()
    {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //get username from JWT token
    public String getUsername(String token)
    {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    //validate Jwt token
    public boolean validateToken(String token)
    {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parse(token);
            return true;
        }
        catch (MalformedJwtException malformedJwtException){
            throw new APIException(HttpStatus.BAD_REQUEST,"Invalid JWT token");
        }
        catch (ExpiredJwtException expiredJwtException){
            throw new APIException(HttpStatus.BAD_REQUEST,"Expired JWT token");
        }
        catch (UnsupportedJwtException unsupportedJwtException){
            throw new APIException(HttpStatus.BAD_REQUEST,"Unsupported JWT token");
        }
        catch (IllegalArgumentException illegalArgumentException){
            throw new APIException(HttpStatus.BAD_REQUEST,"JWT claims string is null or empty");
        }

    }

}
