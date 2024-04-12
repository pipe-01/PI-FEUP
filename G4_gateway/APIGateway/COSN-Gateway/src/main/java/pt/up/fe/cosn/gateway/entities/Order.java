package pt.up.fe.cosn.gateway.entities;

import lombok.Getter;
import lombok.Setter;

public class Order {
    @Getter
    @Setter
    private double latitude;
    @Getter
    @Setter
    private double longitude;
    @Getter
    @Setter
    private String city;

    public Order(double latitude, double longitude, String city) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }
}
