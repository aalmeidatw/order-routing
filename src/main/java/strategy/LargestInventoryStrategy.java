package strategy;

import lombok.Value;
import model.InventoryItem;
import model.Warehouse;
import strategy.model.Strategy;
import java.util.*;
import java.util.stream.Collectors;

@Value
public class LargestInventoryStrategy implements Strategy {

    @Override
    public List<InventoryItem> executeStrategy(List<InventoryItem> inventoryItems, List<Warehouse> warehouseList) {

        return createListOrganizerByLargeUnits(inventoryItems, orderByLargeUnits(inventoryItems));
    }

    private List<InventoryItem> createListOrganizerByLargeUnits(List<InventoryItem> inventoryItems, Map<String, Integer> orderListMap){

        List<InventoryItem> inventoryListOrganizer = new ArrayList<>();
        List<String> wareHouseList = new ArrayList<>(orderListMap.keySet());

        for (String warehouse : wareHouseList) {
            for (InventoryItem inventory: inventoryItems) {
                 if (inventory.getWarehouseName().equals(warehouse)){
                    inventoryListOrganizer.add(inventory);
                }
            }
        }
        return inventoryListOrganizer;
    }

    private Map<String, Integer> orderByLargeUnits(List<InventoryItem> inventoryItems) {

        Map<String, Integer> orderedByLargeUnits = new LinkedHashMap<>();

        Map<String, Integer> groupByLargeUnits = inventoryItems
                .stream()
                .collect(Collectors.groupingBy(InventoryItem::getWarehouseName,
                        Collectors.summingInt(InventoryItem::getQuantityAvailable)));

        groupByLargeUnits.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> orderedByLargeUnits.put(x.getKey(), x.getValue()));

        return orderedByLargeUnits;
    }
}
