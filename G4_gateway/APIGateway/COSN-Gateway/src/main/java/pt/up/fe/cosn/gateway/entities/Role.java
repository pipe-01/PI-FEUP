package pt.up.fe.cosn.gateway.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    @Column(unique=true, nullable = false)
    private String name;
    @Getter
    @Setter
    @Column(unique=true, nullable = false)
    private Long value;

    public Role(String name, Long value){
        this.name = name;
        this.value = value;
    }

    public Role() {

    }
}
