package algorithm;


import model.InventoryItem;
import model.Warehouse;
import model.map.CapacityListMap;
import model.map.RequestListMap;
import model.OrderItem;
import model.dto.Request;
import model.dto.Response;
import model.filter.FilterShippingMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderAlgorithm {
    private FilterShippingMethod filterShippingMethod = new FilterShippingMethod();
    private RequestListMap requestMap = new RequestListMap();
    private CapacityListMap capacityMap = new CapacityListMap();

    public Response execute(Request request){
        Map<String, Integer> requestListMap = requestMap.getRequestList(request);
        Map<String, Integer> capacityListMap = capacityMap.getCapacityList(request);
        List<InventoryItem> inventoryItemListFiltred = filterShippingMethod.getInventoryShippingMethodRequest(request);
        List<InventoryItem> shipping = new ArrayList<>();

        for (OrderItem item : request.getOrderItemsList()) {

            for (InventoryItem inventory : inventoryItemListFiltred) {

                if (isSameProductNameAndQuantityNeededIsMoreThanZero(requestListMap, item, inventory, capacityListMap)) {


                    int valueToInsertInShippingList = Math.min(requestListMap.get(item.getProductName()), inventory.getQuantityAvailable() );


                    int neededQuantity = requestListMap.get(item.getProductName()) - inventory.getQuantityAvailable();

                    insertNewValueInRequestMap(requestListMap, capacityListMap, inventory, item, Math.max(0, neededQuantity), valueToInsertInShippingList);
                    shipping.add(new InventoryItem(inventory.getWarehouseName(), inventory.getProductName(), valueToInsertInShippingList));
                }
            }
        }
        return new Response(shipping);
    }

    private void insertNewValueInRequestMap(Map<String, Integer> requestListMap, Map<String, Integer> capacityListMap, InventoryItem inventoryItem, OrderItem item, int neededQuantity, int value) {
        requestListMap.put(item.getProductName(), neededQuantity);
        capacityListMap.put(inventoryItem.getWarehouseName().toUpperCase(), capacityListMap.get(inventoryItem.getWarehouseName().toUpperCase()) - value );

    }

    private boolean isSameProductNameAndQuantityNeededIsMoreThanZero(Map<String, Integer> requestListMap, OrderItem item, InventoryItem inventory, Map<String, Integer> capacityListMap) {
        return inventory.getProductName().equals(item.getProductName())
                && (requestListMap.get(item.getProductName()) > 0)
                && (capacityListMap.get(inventory.getWarehouseName().toUpperCase())>= 0 );
    }

    public int getMaxCapacity(Warehouse warehouse, int quantityNeeded) {
        return Math.min(warehouse.getCapacity(), quantityNeeded);
    }
}
