package project.valuetogether.global.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.valuetogether.global.security.config.jwt.JwtTokenProvider;
import project.valuetogether.global.security.entity.RefreshToken;
import project.valuetogether.global.security.repository.RefreshTokenRepository;

@RequiredArgsConstructor
@Service
public class UpdateRefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public String updateToken(String email) {
        RefreshToken findRefreshToken = refreshTokenRepository.findByEmail(email);

        String newRefreshToken = jwtTokenProvider.generateRefreshToken(findRefreshToken.getEmail());
        findRefreshToken.updateRefreshToken(newRefreshToken);

        return newRefreshToken;
    }
}
