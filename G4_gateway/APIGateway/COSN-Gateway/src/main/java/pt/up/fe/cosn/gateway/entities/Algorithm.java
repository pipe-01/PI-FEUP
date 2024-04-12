package pt.up.fe.cosn.gateway.entities;

import lombok.Getter;
import lombok.Setter;

public class Algorithm {
    @Getter
    @Setter
    private String algorithm;

    public Algorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
