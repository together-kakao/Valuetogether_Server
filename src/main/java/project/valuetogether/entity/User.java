package project.valuetogether.entity;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String address;

    @NotNull
    private String gender;

    @NotNull
    private String nickname;

    @NotNull
    private String birthYear;

    @NotNull
    @ColumnDefault("ROLE_USER")
    private String role;

    @Builder
    public User(Long id, String email, String password, String name, String phoneNumber, String address, String gender, String nickname, String birthYear) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.gender = gender;
        this.nickname = nickname;
        this.birthYear = birthYear;
    }
}
