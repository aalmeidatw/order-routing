package algorithm;


import exception.ProductIsNotAvailableException;
import model.InventoryItem;
import model.OrderItem;
import model.WarehouseFulfillOrder;
import model.dto.Request;
import model.dto.Response;
import model.filter.FilterShippingMethod;
import model.map.CapacityListMap;
import model.map.RequestListMap;
import strategy.model.Strategy;

import java.util.ArrayList;
import java.util.List;

public class OrderAlgorithm {
    private FilterShippingMethod filterShippingMethod = new FilterShippingMethod();
    private RequestListMap requestMap;
    private CapacityListMap capacityMap = new CapacityListMap();
    private static String ORDER_NOT_COMPLETED = "Order cannot be fulfilled";

    public OrderAlgorithm(FilterShippingMethod filterShippingMethod, RequestListMap requestMap, CapacityListMap capacityMap) {
        this.filterShippingMethod = filterShippingMethod;
        this.requestMap = requestMap;
        this.capacityMap = capacityMap;
    }

    public Response execute(Request request) throws Exception{

        this.requestMap.createRequestMap(request.getOrderItemsList());
        capacityMap.createCapacityMap(request.getWarehouseList());

        List<InventoryItem> filtredInventoryList = getFiltredInventoryList(request);
        List<WarehouseFulfillOrder> warehousesFulfillOrderlList = new ArrayList<>();

        for (OrderItem item : request.getOrderItemsList()) {

            for (InventoryItem inventory : filtredInventoryList) {
                int valueToInsertInShippingList;
                int neededQuantity;

                if (isSameProductNameAndQuantityNeededIsMoreThanZero(requestMap, item, inventory, capacityMap)) {

                     valueToInsertInShippingList = Math.min(requestMap.getProductQuantity(item.getProductName()), inventory.getQuantityAvailable());
                     neededQuantity = requestMap.getProductQuantity(item.getProductName()) - inventory.getQuantityAvailable();

                        if (capacityMap.getProductQuantity(inventory.getWarehouseName()) <= requestMap.getProductQuantity(item.getProductName())) {
                            valueToInsertInShippingList =  Math.min(valueToInsertInShippingList, capacityMap.getProductQuantity(inventory.getWarehouseName()));
                            neededQuantity = requestMap.getProductQuantity(item.getProductName()) - valueToInsertInShippingList;
                        }

                    int newCapacity = getNewCapacityValue(inventory, capacityMap, valueToInsertInShippingList);

                    updateRequestAndCapacityMap(capacityMap, inventory, item, Math.max(0, neededQuantity), newCapacity);
                    warehousesFulfillOrderlList.add(new WarehouseFulfillOrder( inventory.getWarehouseName(), inventory.getProductName(), valueToInsertInShippingList));
                }
            }
        }

        if(!requestMap.isMapCompleted()){
            throw new ProductIsNotAvailableException(ORDER_NOT_COMPLETED);
        }

        return new Response(warehousesFulfillOrderlList);
    }

    private List<InventoryItem> getFiltredInventoryList(Request request){
        Strategy strategy = request.getStrategy();
        List<InventoryItem> inventoryListFiltredByShippingMethod = filterShippingMethod.getInventoryListFiltredByShippingMethodRequest(request);

        return strategy.executeStrategy(inventoryListFiltredByShippingMethod, request.getWarehouseList());
    }

    protected void updateRequestAndCapacityMap(CapacityListMap capacityMap,
                                               InventoryItem inventoryItem, OrderItem item,
                                               int neededQuantity, int newCapacity) {

        this.requestMap.updateProductQuantity(item.getProductName(), neededQuantity);
        capacityMap.updateCapacityQuantity(inventoryItem.getWarehouseName(), newCapacity );
    }

    private boolean isSameProductNameAndQuantityNeededIsMoreThanZero(RequestListMap requestMap, OrderItem item,
                                                                     InventoryItem inventory, CapacityListMap capacityMap) {

        return inventory.getProductName().equals(item.getProductName())
                && (requestMap.isMoreThanZero(item.getProductName()))
                && (capacityMap.isMoreThanZero(inventory.getWarehouseName()));
    }

    protected int getNewCapacityValue(InventoryItem  inventory, CapacityListMap capacityMap, int quantityNeeded) {
        int capacityValue = capacityMap.getProductQuantity(inventory.getWarehouseName());
        return Math.max(0, capacityValue - quantityNeeded);
    }
}
