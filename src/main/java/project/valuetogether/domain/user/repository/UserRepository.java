package project.valuetogether.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.valuetogether.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("select u from User u where (u.nickname = :nicknameOrName or " +
            "u.name = :nicknameOrName) and u.phoneNumber = :phoneNumber")
    Optional<User> findAccount(@Param("nicknameOrName") String nicknameOrName,
                               @Param("phoneNumber") String phoneNumber);

    @Query("select u from User u where u.email = :emailOrPhonenumber or u.phoneNumber = :emailOrPhonenumber")
    Optional<User> verifyUser(@Param("emailOrPhonenumber") String emailOrPhonenumber);
}
