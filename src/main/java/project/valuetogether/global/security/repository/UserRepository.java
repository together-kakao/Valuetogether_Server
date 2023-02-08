package project.valuetogether.global.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.valuetogether.domain.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);
}
