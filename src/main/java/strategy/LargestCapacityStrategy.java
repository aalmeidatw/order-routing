package strategy;


import model.InventoryItem;
import model.Warehouse;
import repository.Repository;
import strategy.model.Strategy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LargestCapacityStrategy implements Strategy {

    @Override
    public List<InventoryItem> executeStrategy(List<InventoryItem> inventoryItems, List<Warehouse> warehouses) {

        return createOrganizerListByCapacity(inventoryItems, orderByCapacity(warehouses));
    }

    private List<InventoryItem> createOrganizerListByCapacity(List<InventoryItem> inventoryItems, Map<String, Integer> orderListMap) {

        List<InventoryItem> inventoryListOrganizer = new ArrayList<>();
        List<String> listOfWarehouseName = new ArrayList<>(orderListMap.keySet());

        for (String warehouse : listOfWarehouseName) {
            for (InventoryItem inventory : inventoryItems) {
                if (inventory.getWarehouseName().toUpperCase().equals(warehouse)) {
                    inventoryListOrganizer.add(inventory);
                }
            }
        }
        return inventoryListOrganizer;
    }

    private Map<String, Integer> orderByCapacity(List<Warehouse> warehouses) {

        Map<String, Integer> orderedByCapacity = new LinkedHashMap<>();

        Map<String, Integer> groupByCapacity = warehouses
                .stream()
                .collect(Collectors.groupingBy(Warehouse::getWarehouseName,
                        Collectors.summingInt(Warehouse::getCapacity)));

        groupByCapacity.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> orderedByCapacity.put(x.getKey(), x.getValue()));

        return orderedByCapacity;
    }
}
