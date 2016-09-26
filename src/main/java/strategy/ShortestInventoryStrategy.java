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
          Map<String, Integer> groupByCapacity = inventoryItems
                .stream()
                .collect(Collectors.groupingBy(InventoryItem::getWarehouseName,
                         Collectors.summingInt(InventoryItem::getQuantityAvailable)));

        Map<String, Integer> orderListMap = orderByLessUnits(groupByCapacity);

        return createListOrganizerByLargeUnits(inventoryItems, orderListMap);
    }


    private List<InventoryItem> createListOrganizerByLargeUnits(List<InventoryItem> inventoryItems, Map<String, Integer> orderListMap) {

        List<InventoryItem> inventoryListOrganizer = new ArrayList<>();

        List<String> listOfWarehouseName = orderListMap
                .entrySet()
                .stream()
                .map(warehouse -> warehouse.getKey())
                .collect(Collectors.toList());

        for (String warehouse : listOfWarehouseName) {
            for (InventoryItem inventory : inventoryItems) {
                if (inventory.getWarehouseName().equals(warehouse)) {
                    inventoryListOrganizer.add(inventory);
                }
            }
        }
        return inventoryListOrganizer;
    }

    private Map<String, Integer> orderByLessUnits(Map<String, Integer> groupCapacity) {
        Map<String, Integer> orderedList = new LinkedHashMap<>();

        groupCapacity.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> orderedList.put(x.getKey(), x.getValue()));

        return orderedList;
    }

}