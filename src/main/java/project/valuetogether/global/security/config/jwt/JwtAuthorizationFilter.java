package project.valuetogether.global.security.config.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import project.valuetogether.domain.entity.User;
import project.valuetogether.global.exception.ExpiredJwtException;
import project.valuetogether.global.exception.InvalidJwtException;
import project.valuetogether.global.security.error.ErrorCode;
import project.valuetogether.global.security.config.auth.PrincipalDetails;
import project.valuetogether.global.security.entity.RefreshToken;
import project.valuetogether.global.security.repository.RefreshTokenRepository;
import project.valuetogether.global.security.repository.UserRepository;
import project.valuetogether.global.security.service.UpdateRefreshTokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private JwtTokenProvider jwtTokenProvider;
    private UpdateRefreshTokenService updateRefreshTokenService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JwtTokenProvider jwtTokenProvider, UpdateRefreshTokenService updateRefreshTokenService) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.updateRefreshTokenService = updateRefreshTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String accessTokenHeader = jwtTokenProvider.resolveAccessToken(request);
        String refreshTokenHeader = jwtTokenProvider.resolveRefreshToken(request);

        String accessToken = jwtTokenProvider.parseToken(accessTokenHeader);
        String refreshToken = jwtTokenProvider.parseToken(refreshTokenHeader);

        if (refreshToken == null) {
            try {
                if (accessToken == null) {
                    throw new InvalidJwtException();
                }
                String email = jwtTokenProvider.getTokenEmail(accessToken);

                RefreshToken findRefreshToken = refreshTokenRepository.findByEmail(email);

                try {
                    jwtTokenProvider.getTokenEmail(findRefreshToken.getRefreshToken());

                } catch (ExpiredJwtException e) {
                    String NewRefreshToken = updateRefreshTokenService.updateToken(email);

                    response.addHeader(JwtProperties.HEADER_ACCESS, JwtProperties.TOKEN_PREFIX + accessToken);
                    response.addHeader(JwtProperties.HEADER_REFRESH, JwtProperties.TOKEN_PREFIX + NewRefreshToken);
                }

                if (email != null) {
                    Optional<User> findUser = userRepository.findByEmail(email);

                    PrincipalDetails principalDetails = new PrincipalDetails(findUser.get());
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            principalDetails,
                            null,
                            principalDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ExpiredJwtException e) {
                request.setAttribute("exception", ErrorCode.EXPIRED_JWT.getMessage());
            } catch (InvalidJwtException e) {
                request.setAttribute("exception", ErrorCode.INVALID_JWT.getMessage());
            } catch (Exception e) {
                request.setAttribute("exception", ErrorCode.NON_EXISTENT.getMessage());
            }
        }
        chain.doFilter(request, response);
    }
}



