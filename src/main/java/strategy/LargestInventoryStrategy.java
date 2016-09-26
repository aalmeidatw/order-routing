package strategy;

import lombok.Value;
import model.InventoryItem;
import strategy.model.Strategy;
import java.util.*;
import java.util.stream.Collectors;

@Value
public class LargestInventoryStrategy implements Strategy {

    @Override
    public List<InventoryItem> executeStrategy(List<InventoryItem> inventoryItems) {

        Map<String, Integer> groupByCapacity = inventoryItems
                .stream()
                .collect(Collectors.groupingBy(InventoryItem::getWarehouseName,
                            Collectors.summingInt(InventoryItem::getQuantityAvailable)));

        Map<String, Integer> orderListMap = orderByMoreUnits(groupByCapacity);

        return createListOrganizerByLargeUnits(inventoryItems, orderListMap);
    }

    private List<InventoryItem> createListOrganizerByLargeUnits(List<InventoryItem> inventoryItems, Map<String, Integer> orderListMap){

        List<InventoryItem> inventoryListOrganizer = new ArrayList<>();

        List<String> listOfWarehouseName = orderListMap
                .entrySet()
                .stream()
                .map(warehouse -> warehouse.getKey())
                .collect(Collectors.toList());

        for (String warehouse : listOfWarehouseName) {
            for (InventoryItem inventory: inventoryItems) {
                 if (inventory.getWarehouseName().equals(warehouse)){
                    inventoryListOrganizer.add(inventory);
                }
            }
        }
        return inventoryListOrganizer;
    }

    private Map<String, Integer> orderByMoreUnits(Map<String, Integer> groupCapacity) {
        Map<String, Integer> orderedList = new LinkedHashMap<>();

        groupCapacity.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> orderedList.put(x.getKey(), x.getValue()));

        return orderedList;
    }
}
