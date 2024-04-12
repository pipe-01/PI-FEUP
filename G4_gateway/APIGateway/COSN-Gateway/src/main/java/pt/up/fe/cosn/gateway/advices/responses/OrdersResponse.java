package pt.up.fe.cosn.gateway.advices.responses;

import lombok.Getter;
import lombok.Setter;
import pt.up.fe.cosn.gateway.entities.Order;

import java.util.List;

public class OrdersResponse {
    @Getter
    @Setter
    private List<Order> orders;

    public OrdersResponse(List<Order> orders){
        this.orders = orders;
    }
}
