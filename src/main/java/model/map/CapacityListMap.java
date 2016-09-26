package model.map;


import model.Warehouse;
import model.dto.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CapacityListMap {
    Map<String, Integer> capacityListMap = new HashMap<>();

    public void createCapacityMap(Request request){
        this.capacityListMap = request.getWarehouseList()
                    .stream()
                    .collect(Collectors.toMap(
                            Warehouse::getWarehouseName, Warehouse::getCapacity));
    }

    public boolean isMoreThanZero(String warehouseName) {
        return capacityListMap.get(warehouseName.toUpperCase()) > 0;
    }

    public int getProductQuantity(String warehouseName) {
        return capacityListMap.get(warehouseName.toUpperCase());
    }

    public void updateCapacityQuantity(String warehouseName, int newCapacity) {
        capacityListMap.put(warehouseName.toUpperCase(), newCapacity);
    }
}
