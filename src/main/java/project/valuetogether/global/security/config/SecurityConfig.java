package project.valuetogether.global.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import project.valuetogether.global.security.config.jwt.CustomAuthenticationEntryPoint;
import project.valuetogether.global.security.config.jwt.JwtAuthenticationFilter;
import project.valuetogether.global.security.config.jwt.JwtAuthorizationFilter;
import project.valuetogether.global.security.config.jwt.JwtTokenProvider;
import project.valuetogether.global.security.config.jwt.RefreshCheckFilter;
import project.valuetogether.global.security.repository.RefreshTokenRepository;
import project.valuetogether.global.security.repository.UserRepository;
import project.valuetogether.global.security.service.UpdateRefreshTokenService;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UpdateRefreshTokenService updateRefreshTokenService;

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new MyCustomDsl())
                .and()
                .authorizeRequests()
                .antMatchers("/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_PROPOSER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/proposer/**")
                .access("hasRole('ROLE_PROPOSER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();

        return http.build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager, refreshTokenRepository, jwtTokenProvider))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository, refreshTokenRepository, jwtTokenProvider, updateRefreshTokenService))
                    .addFilter(new RefreshCheckFilter(authenticationManager, refreshTokenRepository, jwtTokenProvider));
        }
    }
}
