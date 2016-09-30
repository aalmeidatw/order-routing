package algorithm;


import exception.ProductIsNotAvailableException;
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

@Value
public class OrderAlgorithm {
    private FilterShippingMethod filterShippingMethod;
    private RequestListMap requestMap;
    private CapacityListMap capacityMap;
    private static String ORDER_NOT_COMPLETED = "Order cannot be fulfilled";

    public Response execute(Request request) throws Exception{

        requestMap.createRequestMap(request.getOrderItemsList());
        capacityMap.createCapacityMap(request.getWarehouseList());

        List<InventoryItem> filtredInventoryList = getFiltredInventoryList(request);
        List<WarehouseFulfill> warehousesFulfillOrderlList = new ArrayList<>();

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

                    updateRequestAndCapacityMap(requestMap, capacityMap, inventory, item, Math.max(0, neededQuantity), newCapacity);
                    warehousesFulfillOrderlList.add(new WarehouseFulfill( inventory.getWarehouseName(), inventory.getProductName(), valueToInsertInShippingList));
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

    protected void updateRequestAndCapacityMap(RequestListMap requestMap, CapacityListMap capacityMap,
                                               InventoryItem inventoryItem, OrderItem item,
                                               int neededQuantity, int newCapacity) {

        requestMap.updateProductQuantity(item.getProductName(), neededQuantity);
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
