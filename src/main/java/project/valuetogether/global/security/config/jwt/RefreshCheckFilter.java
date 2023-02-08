package project.valuetogether.global.security.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import project.valuetogether.global.exception.ExpiredJwtException;
import project.valuetogether.global.exception.InvalidJwtException;
import project.valuetogether.global.security.error.ErrorCode;
import project.valuetogether.global.exception.NonExistentException;
import project.valuetogether.global.security.dto.LoginResponseDto;
import project.valuetogether.global.security.entity.RefreshToken;
import project.valuetogether.global.security.repository.RefreshTokenRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class RefreshCheckFilter extends BasicAuthenticationFilter{

    private RefreshTokenRepository refreshTokenRepository;
    private JwtTokenProvider jwtTokenProvider;

    public RefreshCheckFilter(AuthenticationManager authenticationManager, RefreshTokenRepository refreshTokenRepository, JwtTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessTokenHeader = jwtTokenProvider.resolveAccessToken(request);
        String refreshTokenHeader = jwtTokenProvider.resolveRefreshToken(request);

        String accessToken = jwtTokenProvider.parseToken(accessTokenHeader);
        String refreshToken = jwtTokenProvider.parseToken(refreshTokenHeader);

        if (accessToken != null && refreshToken != null) {
            try {
                jwtTokenProvider.getTokenEmail(accessToken);
            } catch (ExpiredJwtException e) {
                request.setAttribute("exception", ErrorCode.UNKNOWN_ERROR.getMessage());
            } catch (InvalidJwtException e) {
                request.setAttribute("exception", ErrorCode.INVALID_JWT.getMessage());
            }
            try {
                Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);
                if (findRefreshToken.isEmpty()) {
                    throw new NonExistentException();
                }

                if (refreshToken.equals(findRefreshToken.get().getRefreshToken())) {
                    String findEmail = jwtTokenProvider.getTokenEmail(refreshToken);

                    String NewAccessToken = jwtTokenProvider.generateAccessToken(findEmail);
                    LoginResponseDto loginResponseDto = new LoginResponseDto(NewAccessToken, refreshToken);

                    ObjectMapper om = new ObjectMapper();

                    String result = om.writeValueAsString(loginResponseDto);

                    System.out.println("엑세스 재발급이용");
                    response.addHeader(JwtProperties.HEADER_ACCESS, JwtProperties.TOKEN_PREFIX + NewAccessToken);
                    response.addHeader(JwtProperties.HEADER_REFRESH, JwtProperties.TOKEN_PREFIX + refreshToken);
                    response.getWriter().write(result);
                }

            } catch (NonExistentException e) {
                request.setAttribute("exception", ErrorCode.NON_EXISTENT.getMessage());
            } catch (ExpiredJwtException e) {
                request.setAttribute("exception", ErrorCode.EXPIRED_JWT.getMessage());
            } catch (InvalidJwtException e) {
                request.setAttribute("exception", ErrorCode.INVALID_JWT.getMessage());
            } catch (Exception e) {
                request.setAttribute("exception", ErrorCode.INVALID_JWT.getMessage());
            }
        }
        chain.doFilter(request, response);
    }
}


