package model;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Getter
@Builder
public class WarehouseFulfillOrder {
    private String warehouseName;
    private  String productName;
    private int quantity;

    public WarehouseFulfillOrder(String warehouseName, String productName, int quantity) {
        this.warehouseName = warehouseName;
        this.productName = productName;
        this.quantity = quantity;
    }
}
