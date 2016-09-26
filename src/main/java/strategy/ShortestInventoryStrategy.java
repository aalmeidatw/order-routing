package strategy;

import model.InventoryItem;
import strategy.model.Strategy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShortestInventoryStrategy implements Strategy {

    @Override
    public List<InventoryItem> executeStrategy(List<InventoryItem> inventoryItems) {

        return createOrganizerListByLessUnits(inventoryItems, orderByLessUnits(inventoryItems));
    }


    private List<InventoryItem> createOrganizerListByLessUnits(List<InventoryItem> inventoryItems, Map<String, Integer> orderListMap) {

        List<InventoryItem> inventoryListOrganizer = new ArrayList<>();
        List<String> listOfWarehouseName = new ArrayList<>(orderListMap.keySet());

        for (String warehouse : listOfWarehouseName) {
            for (InventoryItem inventory : inventoryItems) {
                if (inventory.getWarehouseName().equals(warehouse)) {
                    inventoryListOrganizer.add(inventory);
                }
            }
        }
        return inventoryListOrganizer;
    }

    private Map<String, Integer> orderByLessUnits(List<InventoryItem> inventoryItems) {
        Map<String, Integer> orderedByLessUnits = new LinkedHashMap<>();

        Map<String, Integer> groupByLessUnits = inventoryItems
                .stream()
                .collect(Collectors.groupingBy(InventoryItem::getWarehouseName,
                        Collectors.summingInt(InventoryItem::getQuantityAvailable)));


        groupByLessUnits.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> orderedByLessUnits.put(x.getKey(), x.getValue()));

        return orderedByLessUnits;


    }

}