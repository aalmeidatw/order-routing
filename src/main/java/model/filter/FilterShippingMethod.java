package model.filter;

import model.InventoryItem;
import model.ShippingMethod;
import model.Warehouse;
import model.dto.Request;
import java.util.List;
import java.util.stream.Collectors;

public class FilterShippingMethod {

    public List<InventoryItem> getInventoryListFiltredByShippingMethodRequest(Request request){

        return request.getInventoryItems().stream()
                                .filter(inventoryItem -> isShippingMethodSuported(inventoryItem,
                                        request.getShippingMethodMethod(),
                                        request.getWarehouseList()))
                                .collect(Collectors.toList());
     }

    private boolean isShippingMethodSuported(InventoryItem inventoryItem,
                                             ShippingMethod shippingMethod,
                                             List<Warehouse> warehouseList) {
        return warehouseList.stream()
                            .anyMatch(warehouse ->  (warehouse.getWarehouseName().equals(inventoryItem.getWarehouseName().toUpperCase()) &&
                                                    (warehouse.getShippingMethodList().contains(shippingMethod))));
    }
}
