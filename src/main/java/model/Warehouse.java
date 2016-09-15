package model;

import lombok.Getter;
import lombok.Value;
import java.util.List;

@Getter
@Value
public class Warehouse {
    private String warehouseName;
    private List<Shipping> shippingList;
    private int capacity;

    public Warehouse(String warehouseName, List<Shipping> shippingList, int capacity) {
        this.warehouseName = warehouseName;
        this.shippingList = shippingList;
        this.capacity = capacity;
    }
}
