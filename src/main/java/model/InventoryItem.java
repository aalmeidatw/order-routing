package model;

import lombok.Getter;
import lombok.Value;

@Getter
@Value
public class InventoryItem {
    private String warehouseName;
    private String productName;
    private int quantityAvailable;

    public InventoryItem(String warehouseName, String productName, int quantityAvailable) {
        this.warehouseName = warehouseName;
        this.productName = productName;
        this.quantityAvailable = quantityAvailable;
    }
}
