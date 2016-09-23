package algorithm;


import lombok.Value;
import model.InventoryItem;
import model.WarehouseFulfill;
import model.map.CapacityListMap;
import model.map.RequestListMap;
import model.OrderItem;
import model.dto.Request;
import model.dto.Response;
import model.filter.FilterShippingMethod;
import strategy.model.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Value
public class OrderAlgorithm {
    private FilterShippingMethod filterShippingMethod;
    private RequestListMap requestMap;
    private CapacityListMap capacityMap;


    public Response execute(Request request, RequestListMap requestMap){

        requestMap.createRequestMap(request);

        Map<String, Integer> capacityListMap = capacityMap.getCapacityList(request);

        List<InventoryItem> filtredInventoryList = getFiltredInventoryList(request);
        List<WarehouseFulfill> warehouseFulfillList = new ArrayList<>();

        for (OrderItem item : request.getOrderItemsList()) {

            for (InventoryItem inventory : filtredInventoryList) {
                int valueToInsertInShippingList;
                int neededQuantity;

                if (isSameProductNameAndQuantityNeededIsMoreThanZero(requestMap, item, inventory, capacityListMap)) {

                     valueToInsertInShippingList = Math.min(requestMap.getProductQuantity(item.getProductName()), inventory.getQuantityAvailable());
                     neededQuantity = requestMap.getProductQuantity(item.getProductName()) - inventory.getQuantityAvailable();

                        if (capacityListMap.get(inventory.getWarehouseName().toUpperCase()) <= requestMap.getProductQuantity(item.getProductName())) {
                            valueToInsertInShippingList =  Math.min(valueToInsertInShippingList, capacityListMap.get(inventory.getWarehouseName().toUpperCase()));
                            neededQuantity = requestMap.getProductQuantity(item.getProductName()) - valueToInsertInShippingList;
                        }

                    int newCapacity = getNewCapacityValue(inventory, capacityListMap, valueToInsertInShippingList);

                    updateRequestAndCapacityMap(requestMap, capacityListMap, inventory, item, Math.max(0, neededQuantity), newCapacity);
                    warehouseFulfillList.add(new WarehouseFulfill( inventory.getWarehouseName(), inventory.getProductName(), valueToInsertInShippingList));
                }
            }
        }
        return new Response(warehouseFulfillList); //return
    }

    public Optional<OrderItem> verifyIfProductListIsNeeded(Map<String, Integer> requestListMap, Request request){

        return request.getOrderItemsList().stream()
                .filter(item ->    requestListMap.get(item.getProductName()) > 0 )
                .findFirst();
    }


    private List<InventoryItem> getFiltredInventoryList(Request request){
        Strategy strategy = request.getStrategy();
        List<InventoryItem> inventoryListFiltredByShippingMethod = filterShippingMethod.getInventoryListFiltredByShippingMethodRequest(request);

        return strategy.executeStrategy(inventoryListFiltredByShippingMethod);
    }

    protected void updateRequestAndCapacityMap(RequestListMap requestMap, Map<String, Integer> capacityListMap,
                                             InventoryItem inventoryItem, OrderItem item,
                                             int neededQuantity, int newCapacity) {

        requestMap.updateProductQuantity(item.getProductName(), neededQuantity);
        capacityListMap.put(inventoryItem.getWarehouseName().toUpperCase(), newCapacity );
    }

    private boolean isSameProductNameAndQuantityNeededIsMoreThanZero(RequestListMap requestMap, OrderItem item,
                                                                     InventoryItem inventory, Map<String, Integer> capacityListMap) {

        return inventory.getProductName().equals(item.getProductName())
                && (requestMap.isMoreThanZero(item.getProductName()))
                && (capacityListMap.get(inventory.getWarehouseName().toUpperCase()) >= 0);
    }

    protected int getNewCapacityValue(InventoryItem  inventory, Map<String, Integer> capacityListMap, int quantityNeeded) {
        int capacityValue = capacityListMap.get(inventory.getWarehouseName().toUpperCase());
        return Math.max(0, capacityValue - quantityNeeded);
    }
}
