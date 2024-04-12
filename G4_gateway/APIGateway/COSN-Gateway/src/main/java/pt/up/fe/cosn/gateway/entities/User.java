package pt.up.fe.cosn.gateway.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import pt.up.fe.cosn.gateway.utils.Utils;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    @Column(unique=true, nullable = false)
    private String email;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(nullable = false)
    private Role role;
    @JsonIgnore
    @Getter
    @Setter
    private LocalDateTime creationDate;
    @JsonIgnore
    @Getter
    @Setter
    private LocalDateTime lastLoginDate;

    public User(String email, String password, Boolean alreadyEncrypted) {
        if(!alreadyEncrypted){
            password = Utils.encryptPassword(password);
        }

        this.email = email;
        this.password = password;
        this.creationDate = LocalDateTime.now();
        this.lastLoginDate = LocalDateTime.now();
    }

    public User(String email, String password, Role role, Boolean alreadyEncrypted) {
        if(!alreadyEncrypted){
            password = Utils.encryptPassword(password);
        }

        this.role = role;
        this.email = email;
        this.password = password;
        this.creationDate = LocalDateTime.now();
        this.lastLoginDate = LocalDateTime.now();
    }

    public User() {

    }

    public void refreshLastLogin(){
        this.lastLoginDate = LocalDateTime.now();
    }
}

