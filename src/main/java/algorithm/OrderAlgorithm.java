package algorithm;


import model.InventoryItem;
import model.OrderItem;
import model.dto.Request;
import model.dto.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class OrderAlgorithm {

    public Response execute(Request request){

        Map<String, Integer> requestListMap = request.getOrderItemsList()
                .stream()
                .collect(Collectors.toMap(OrderItem::getProductName, OrderItem::getQuantityNeeded));

        List<InventoryItem> shipping = new ArrayList<>();

        for (OrderItem item : request.getOrderItemsList()) {

            for (InventoryItem inventory : request.getInventoryItems()) {

                if (isSameProductNameAndQuantityNeededIsMoreThanZero(requestListMap, item, inventory)) {

                    int minValue = Math.min(requestListMap.get(item.getProductName()), inventory.getQuantityAvailable() );
                    int neededQuantity = requestListMap.get(item.getProductName()) - inventory.getQuantityAvailable();

                    insertNewValueInRequestMap(requestListMap, item, Math.max(0, neededQuantity));
                    shipping.add(new InventoryItem(inventory.getWarehouseName(), inventory.getProductName(), minValue));
                }
            }
        }
        return new Response(shipping);
    }

    private void insertNewValueInRequestMap(Map<String, Integer> requestListMap, OrderItem item, int value) {
        requestListMap.put(item.getProductName(), value);
    }

    private boolean isSameProductNameAndQuantityNeededIsMoreThanZero(Map<String, Integer> requestListMap, OrderItem item, InventoryItem inventory) {
        return inventory.getProductName().equals(item.getProductName()) && (requestListMap.get(item.getProductName()) > 0);
    }

}
