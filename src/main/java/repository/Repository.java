package repository;


import lombok.Getter;
import lombok.Value;
import model.ShippingMethod;
import model.Warehouse;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@Value
@Getter
public class Repository {
    private static List<ShippingMethod> BRAZIL_SHIPPING_METHOD = asList(ShippingMethod.DHL, ShippingMethod.FEDEX);
    private static List<ShippingMethod> FRANCE_SHIPPING_METHOD = asList(ShippingMethod.DHL, ShippingMethod.FEDEX, ShippingMethod.UPS);
    private static List<ShippingMethod> SOUTH_AFRICA_SHIPPING_METHOD = asList(ShippingMethod.FEDEX, ShippingMethod.UPS);
    private static List<ShippingMethod> CHINA_SHIPPING_METHOD = singletonList(ShippingMethod.DHL);
    private static List<ShippingMethod> CANADA_SHIPPING_METHOD = singletonList(ShippingMethod.FEDEX);

    private static Warehouse BRAZIL_WAREHOUSE = Warehouse.builder()
            .warehouseName("BRAZIL")
            .shippingMethodList(BRAZIL_SHIPPING_METHOD)
            .capacity(15).build();

    private static Warehouse FRANCE_WAREHOUSE = Warehouse.builder()
            .warehouseName("FRANCE")
            .shippingMethodList(FRANCE_SHIPPING_METHOD)
            .capacity(10).build();

    private static Warehouse SOUTH_AFRICA_WAREHOUSE = Warehouse.builder()
            .warehouseName("SOUTH AFRICA")
            .shippingMethodList(SOUTH_AFRICA_SHIPPING_METHOD)
            .capacity(10).build();

    private static Warehouse CHINA_WAREHOUSE = Warehouse.builder()
            .warehouseName("CHINA")
            .shippingMethodList(CHINA_SHIPPING_METHOD)
            .capacity(20).build();

    private static Warehouse CANADA_WAREHOUSE = Warehouse.builder()
            .warehouseName("CANADA")
            .shippingMethodList(CANADA_SHIPPING_METHOD)
            .capacity(5).build();

    public  List<Warehouse> getWarehouseRepository(){
        return asList(BRAZIL_WAREHOUSE, FRANCE_WAREHOUSE, SOUTH_AFRICA_WAREHOUSE, CHINA_WAREHOUSE, CANADA_WAREHOUSE);
    }
}
