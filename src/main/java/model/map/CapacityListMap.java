package model.map;


import model.Warehouse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CapacityListMap {
    private Map<String, Integer> capacityListMap = new HashMap<>();
    private final int NONE_QUANTITY = 0;

    public void createCapacityMap(List<Warehouse> warehouseList){
        this.capacityListMap = warehouseList
                    .stream()
                    .collect(Collectors.toMap(
                            Warehouse::getWarehouseName, Warehouse::getCapacity));
    }

    public boolean isMoreThanZero(String warehouseName) {
        return capacityListMap.get(warehouseName.toUpperCase()) > NONE_QUANTITY;
    }

    public int getProductQuantity(String warehouseName) {
        return capacityListMap.get(warehouseName.toUpperCase());
    }

    public void updateCapacityQuantity(String warehouseName, int newCapacity) {
        capacityListMap.put(warehouseName.toUpperCase(), newCapacity);
    }
}
