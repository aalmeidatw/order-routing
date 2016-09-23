package model;

import lombok.Getter;
import lombok.Value;

@Value
@Getter
public class WarehouseFulfill {
    private String warehouseName;
    private  String productName;
    private int quantity;

    public WarehouseFulfill(String warehouseName, String productName, int quantity) {
        this.warehouseName = warehouseName;
        this.productName = productName;
        this.quantity = quantity;
    }
}
