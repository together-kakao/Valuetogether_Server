package project.valuetogether.global.security.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.valuetogether.global.security.config.auth.PrincipalDetails;
import project.valuetogether.global.security.dto.LoginRequestDto;
import project.valuetogether.global.security.dto.LoginResponseDto;
import project.valuetogether.global.security.entity.RefreshToken;
import project.valuetogether.global.security.repository.RefreshTokenRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ObjectMapper om = new ObjectMapper();
        LoginRequestDto loginRequestDto = null;
        try {
            loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        ObjectMapper om = new ObjectMapper();

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String accessToken = jwtTokenProvider.generateAccessToken(principalDetails.getUsername());

        String refreshToken = jwtTokenProvider.generateRefreshToken(principalDetails.getUsername());

        refreshTokenRepository.save(RefreshToken.builder()
                .refreshToken(refreshToken)
                .email(principalDetails.getUsername())
                .build());

        LoginResponseDto loginResponseDto = new LoginResponseDto(accessToken, refreshToken);

        String result = om.writeValueAsString(loginResponseDto);

        response.addHeader(JwtProperties.HEADER_ACCESS, JwtProperties.TOKEN_PREFIX+accessToken);
        response.addHeader(JwtProperties.HEADER_REFRESH, JwtProperties.TOKEN_PREFIX+refreshToken);
        response.getWriter().write(result);
    }
}
