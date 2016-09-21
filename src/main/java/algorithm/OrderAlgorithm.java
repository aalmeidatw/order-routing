package algorithm;


import lombok.Value;
import model.InventoryItem;
import model.map.CapacityListMap;
import model.map.RequestListMap;
import model.OrderItem;
import model.dto.Request;
import model.dto.Response;
import model.filter.FilterShippingMethod;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Value
public class OrderAlgorithm {
    private FilterShippingMethod filterShippingMethod;
    private RequestListMap requestMap;
    private CapacityListMap capacityMap;


    public Response execute(Request request){
        Map<String, Integer> requestListMap = requestMap.getRequestList(request);
        Map<String, Integer> capacityListMap = capacityMap.getCapacityList(request);
        List<InventoryItem> inventoryItemListFiltred = filterShippingMethod.getInventoryShippingMethodRequest(request);
        List<InventoryItem> shipping = new ArrayList<>();

        for (OrderItem item : request.getOrderItemsList()) {

            for (InventoryItem inventory : inventoryItemListFiltred) {
                int valueToInsertInShippingList;
                int neededQuantity;

                if (isSameProductNameAndQuantityNeededIsMoreThanZero(requestListMap, item, inventory, capacityListMap)) {

                     valueToInsertInShippingList = Math.min(requestListMap.get(item.getProductName()), inventory.getQuantityAvailable());
                     neededQuantity = requestListMap.get(item.getProductName()) - inventory.getQuantityAvailable();

                        if (capacityListMap.get(inventory.getWarehouseName().toUpperCase()) <= requestListMap.get(item.getProductName())) {
                            valueToInsertInShippingList = capacityListMap.get(inventory.getWarehouseName().toUpperCase());
                            neededQuantity = requestListMap.get(item.getProductName()) - valueToInsertInShippingList;
                        }

                    int newCapacity = getNewCapacityValue(inventory, capacityListMap, valueToInsertInShippingList);

                    insertNewValueInRequestMap(requestListMap, capacityListMap, inventory, item, Math.max(0, neededQuantity), newCapacity);
                    shipping.add(new InventoryItem(inventory.getWarehouseName(), inventory.getProductName(), valueToInsertInShippingList));
                }
            }
        }
        return new Response(shipping);
    }

    private void insertNewValueInRequestMap(Map<String, Integer> requestListMap, Map<String, Integer> capacityListMap,
                                            InventoryItem inventoryItem, OrderItem item,
                                            int neededQuantity, int newCapacity) {

        requestListMap.put(item.getProductName(), neededQuantity);
        capacityListMap.put(inventoryItem.getWarehouseName().toUpperCase(), newCapacity );
    }

    private boolean isSameProductNameAndQuantityNeededIsMoreThanZero(Map<String, Integer> requestListMap, OrderItem item,
                                                                     InventoryItem inventory, Map<String, Integer> capacityListMap) {

        return inventory.getProductName().equals(item.getProductName())
                && (requestListMap.get(item.getProductName()) > 0)
                && (capacityListMap.get(inventory.getWarehouseName().toUpperCase()) >= 0);
    }

    protected int getNewCapacityValue(InventoryItem  inventory, Map<String, Integer> capacityListMap, int quantityNeeded) {
        int capacityValue = capacityListMap.get(inventory.getWarehouseName().toUpperCase());
        return Math.max(0, capacityValue - quantityNeeded);
    }
}
