package model;

import lombok.Getter;
import lombok.Value;

@Getter
@Value
public class ShippingMethod {
    private String shippingMethodName;

    public ShippingMethod(String shippingMethodName) {
        this.shippingMethodName = shippingMethodName;
    }


}
