package model.filter;

import model.InventoryItem;
import model.ShippingMethod;
import model.Warehouse;
import model.dto.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterShippingMethod {
    private List<InventoryItem> filtredInventoryList = new ArrayList<>();

    public void createInventoryListFiltredByShippingMethodRequest(Request request){

        List<InventoryItem > filtredList = request.getInventoryItems().stream()
                                .filter(inventoryItem -> isShippingMethodSuported(inventoryItem,
                                        request.getShippingMethodMethod(),
                                        request.getWarehouseList()))
                                .collect(Collectors.toList());

        this.filtredInventoryList = request.getStrategy().executeStrategy(filtredList, request.getWarehouseList());
     }

    private boolean isShippingMethodSuported(InventoryItem inventoryItem,
                                             ShippingMethod shippingMethod,
                                             List<Warehouse> warehouseList) {
        return warehouseList.stream()
                            .anyMatch(warehouse ->  (warehouse.getWarehouseName().equals(inventoryItem.getWarehouseName().toUpperCase()) &&
                                                    (warehouse.getShippingMethodList().contains(shippingMethod))));
    }

    public List<InventoryItem> getFiltredInventoryList() {
        return filtredInventoryList;
    }
}
