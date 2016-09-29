package model.map;

import model.OrderItem;
import model.dto.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestListMap {
    private Map<String, Integer> requestListMap = new HashMap<>();
    private final int NONE_QUANTITY = 0;
    private boolean isCompleted;

    public void createRequestMap(Request request){
        this.requestListMap = request.getOrderItemsList()
                            .stream()
                            .collect(Collectors.toMap(
                                        OrderItem::getProductName, OrderItem::getQuantityNeeded));
    }

    public boolean isMoreThanZero(String productName) {
        return requestListMap.get(productName) > NONE_QUANTITY;
    }

    public int getProductQuantity(String productName) {
        return requestListMap.get(productName);
    }

    public void updateProductQuantity(String productName, int neededQuantity) {
        requestListMap.put(productName, neededQuantity);
    }

    public boolean isMapCompleted(){
        requestListMap.forEach((item , quantityNeeded)-> isCompleted = (quantityNeeded <= NONE_QUANTITY));
         return isCompleted;
    }
}

