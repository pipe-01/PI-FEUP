package pt.up.fe.cosn.gateway.requests;

import lombok.Getter;
import lombok.Setter;

public class OrderRequest {
    @Getter
    @Setter
    private double latitude;
    @Getter
    @Setter
    private double longitude;
}
