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

import java.util.ArrayList;
import java.util.List;


public class OrderAlgorithm {
    private FilterShippingMethod filterShippingMethod;
    private RequestListMap requestMap;
    private CapacityListMap capacityMap;
    private final static String ORDER_NOT_COMPLETED = "Order cannot be fulfilled";
    private List<WarehouseFulfillOrder> warehousesFulfillOrderList = new ArrayList<>();

    public OrderAlgorithm(FilterShippingMethod filterShippingMethod, RequestListMap requestMap, CapacityListMap capacityMap) {
        this.filterShippingMethod = filterShippingMethod;
        this.requestMap = requestMap;
        this.capacityMap = capacityMap;
    }

    public Response execute(Request request) throws Exception{
        createMapsAndFiltredInventoryList(request);

        int valueToInsertInShippingList;
        int neededQuantity;

        for (OrderItem item : request.getOrderItemsList()) {

            for (InventoryItem inventory : filterShippingMethod.getFiltredInventoryList()) {

                if (isSameProductNameAndQuantityNeededIsMoreThanZero(item, inventory)) {

                     valueToInsertInShippingList = Math.min(requestMap.getProductQuantity(item.getProductName()), inventory.getQuantityAvailable());
                     neededQuantity = requestMap.getProductQuantity(item.getProductName()) - inventory.getQuantityAvailable();

                        if (capacityMap.getProductQuantity(inventory.getWarehouseName()) <= requestMap.getProductQuantity(item.getProductName())) {
                            valueToInsertInShippingList =  Math.min(valueToInsertInShippingList, capacityMap.getProductQuantity(inventory.getWarehouseName()));
                            neededQuantity = requestMap.getProductQuantity(item.getProductName()) - valueToInsertInShippingList;
                        }
                    int newCapacity = getNewCapacityValue(inventory, valueToInsertInShippingList);
                    updateRequestAndCapacityMap(inventory, item, Math.max(0, neededQuantity), newCapacity);

                    warehousesFulfillOrderList.add(WarehouseFulfillOrder.builder()
                            .warehouseName(inventory.getWarehouseName())
                            .productName(inventory.getProductName())
                            .quantity(valueToInsertInShippingList).build());
                }
            }
        }
        if(!requestMap.isMapCompleted()){
            throw new ProductIsNotAvailableException(ORDER_NOT_COMPLETED);
        }

        return new Response(warehousesFulfillOrderList);
    }

    protected void createMapsAndFiltredInventoryList(Request request) {
        this.requestMap.createRequestMap(request.getOrderItemsList());
        this.capacityMap.createCapacityMap(request.getWarehouseList());
        this.filterShippingMethod.createInventoryListFiltredByShippingMethodRequest(request);
    }

    protected void updateRequestAndCapacityMap(InventoryItem inventoryItem, OrderItem item,
                                               int neededQuantity, int newCapacity) {

        this.requestMap.updateProductQuantity(item.getProductName(), neededQuantity);
        this.capacityMap.updateCapacityQuantity(inventoryItem.getWarehouseName(), newCapacity );
    }

    private boolean isSameProductNameAndQuantityNeededIsMoreThanZero(OrderItem item, InventoryItem inventory) {

        return inventory.getProductName().equals(item.getProductName())
                && (this.requestMap.isMoreThanZero(item.getProductName()))
                && (this.capacityMap.isMoreThanZero(inventory.getWarehouseName()));
    }

    private int getNewCapacityValue(InventoryItem  inventory, int quantityNeeded) {
        int capacityValue = this.capacityMap.getProductQuantity(inventory.getWarehouseName());
        return Math.max(0, capacityValue - quantityNeeded);
    }
}
