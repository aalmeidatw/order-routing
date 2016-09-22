package strategy;

import lombok.Value;
import model.InventoryItem;
import strategy.model.Strategy;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Value
public class LargestStrategy implements Strategy {

    @Override
    public List<InventoryItem> execute(List<InventoryItem> inventoryItems) {

        Map<String, Integer> groupCapacity = inventoryItems
                .stream()
                .collect(Collectors.groupingBy(InventoryItem::getWarehouseName,
                            Collectors.summingInt(InventoryItem::getQuantityAvailable)));

        Map<String, Integer> orderListMap = orderByValue(groupCapacity);

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

    private static <K extends Comparable<? super K>, V> Map<K, V> orderByValue(Map<K, V> groupCapacityMap) {

        Map<K, V> orderResult = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> sequentialStream = groupCapacityMap.entrySet().stream();

        sequentialStream.sorted(Map.Entry
                                   .comparingByKey())
                                   .forEachOrdered(order -> orderResult.put(order.getKey(), order.getValue()));
        return orderResult;
    }
}
