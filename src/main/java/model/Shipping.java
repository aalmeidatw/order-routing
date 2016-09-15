package model;

import lombok.Getter;
import lombok.Value;

@Getter
@Value
public class Shipping {
    private String shippingName;

    public Shipping(String shippingName) {
        this.shippingName = shippingName;
    }


}
