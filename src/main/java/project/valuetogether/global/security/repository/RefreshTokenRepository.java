package project.valuetogether.global.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.valuetogether.global.security.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    RefreshToken findByEmail(String email);
}
