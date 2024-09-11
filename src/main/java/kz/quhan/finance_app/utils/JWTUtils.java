package kz.quhan.finance_app.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtils {
    @Value("${jwt_access_secret}")
    private String accessSecret;

    @Value("${jwt_join_secret}")
    private String joinSecret;

    public String generateAccessToken(String login) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withClaim("login", login)
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(new Date().toInstant().plusSeconds(86400)))
                .sign(Algorithm.HMAC256(accessSecret));
    }

    public String generateJoinToken(Integer billId) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withClaim("billId", billId)
                .withIssuedAt(new Date())
//                .withExpiresAt(Date.from(new Date().toInstant().plusSeconds(86400)))
                .sign(Algorithm.HMAC256(joinSecret));
    }

    public String validateJoinTokenAndRetrieveBillId(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(joinSecret)).build();

        DecodedJWT jwt = verifier.verify(token);

        return String.valueOf(jwt.getClaim("billId"));
    }

    public String validateAccessTokenAndRetrieveSubject(String token)throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(accessSecret))
                .build();

        DecodedJWT jwt = verifier.verify(token);

        return jwt.getClaim("login").asString();
    }
}
