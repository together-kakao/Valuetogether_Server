package project.valuetogether.global.security.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.valuetogether.global.exception.ExpiredJwtException;
import project.valuetogether.global.exception.InvalidJwtException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    public String generateAccessToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("email", email)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    public String generateRefreshToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME))
                .withClaim("email", email)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String bearer = request.getHeader(JwtProperties.HEADER_ACCESS);
        return bearer;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        String bearer = request.getHeader(JwtProperties.HEADER_REFRESH);
        return bearer;
    }

    public String parseToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(JwtProperties.TOKEN_PREFIX))
            return bearerToken.replace(JwtProperties.TOKEN_PREFIX, "");

        return null;
    }

    public String getTokenEmail(String token) {
        try {
            return JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                    .getClaim("email").asString();
        } catch (TokenExpiredException e) {
            throw new ExpiredJwtException();
        } catch (Exception e) {
            throw new InvalidJwtException();
        }
    }
}
