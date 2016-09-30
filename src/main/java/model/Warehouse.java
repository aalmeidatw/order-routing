package model;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.List;

@Getter
@Value
@Builder
public class Warehouse {
    private String warehouseName;
    private List<ShippingMethod> shippingMethodList;
    private int capacity;

    public Warehouse(String warehouseName, List<ShippingMethod> shippingMethodList, int capacity) {
        this.warehouseName = warehouseName;
        this.shippingMethodList = shippingMethodList;
        this.capacity = capacity;
    }



}
