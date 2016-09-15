package model;

import lombok.Getter;
import lombok.Value;

@Getter
@Value
public class OrderItem {
    private String productName;
    private int quantityNeeded;

    public OrderItem(String productName, int quantityNeeded) {
        this.productName = productName;
        this.quantityNeeded = quantityNeeded;
    }
}
