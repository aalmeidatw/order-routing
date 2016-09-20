package model.filter;


import lombok.Value;
import model.InventoryItem;
import model.ShippingMethod;
import model.Warehouse;
import model.dto.Request;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class FilterShippingMethod {


    public List<InventoryItem> getInventoryShippingMethodRequest(Request request){


        return request.getInventoryItems().stream()
                                .filter(inventoryItem -> isShippingMethodIsSuported(inventoryItem,
                                        request.getShippingMethodMethod(),
                                        request.getWarehouseList()))
                                .collect(Collectors.toList());

     }

    protected boolean isShippingMethodIsSuported(InventoryItem inventoryItem,
                                           ShippingMethod shippingMethod,
                                           List<Warehouse> warehouseList) {

        return warehouseList.stream()
                            .anyMatch(warehouse ->  (warehouse.getWarehouseName().equals(inventoryItem.getWarehouseName().toUpperCase()) &&
                                                    (warehouse.getShippingMethodList().contains(shippingMethod))));

    }
}
