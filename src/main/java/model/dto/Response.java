package model.dto;

import lombok.Getter;
import lombok.Value;
import model.WarehouseFulfillOrder;

import java.util.List;

@Getter
@Value
public class Response {
    private List<WarehouseFulfillOrder> warehouseFulfillOrders;

    public Response(List<WarehouseFulfillOrder> warehouseFulfillOrders) {
        this.warehouseFulfillOrders = warehouseFulfillOrders;
    }
}
