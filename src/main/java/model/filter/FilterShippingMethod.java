package model.filter;


import lombok.Value;
import model.InventoryItem;
import model.ShippingMethod;
import model.Warehouse;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class FilterShippingMethod {


    public List<InventoryItem> getInventoryShippingMethodRequest(List<InventoryItem> inventoryItemList,
                                                                 ShippingMethod shippingMethod,
                                                                 List<Warehouse> warehouseList){


        return inventoryItemList.stream()
                                .filter(inventoryItem -> isShippingMethodIsSuported(inventoryItem, shippingMethod, warehouseList))
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
