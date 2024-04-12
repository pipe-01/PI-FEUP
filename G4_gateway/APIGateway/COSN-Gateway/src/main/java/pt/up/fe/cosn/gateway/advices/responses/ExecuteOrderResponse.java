package pt.up.fe.cosn.gateway.advices.responses;

import lombok.Getter;
import lombok.Setter;
import pt.up.fe.cosn.gateway.entities.Order;

public class ExecuteOrderResponse {
    @Getter
    @Setter
    private Order order;

    public ExecuteOrderResponse(Order order){
        this.order = order;
    }
}
