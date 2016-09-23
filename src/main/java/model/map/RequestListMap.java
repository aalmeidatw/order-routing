package model.map;

import model.OrderItem;
import model.dto.Request;
import model.dto.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestListMap {
    private Map<String, Integer> requestListMap = new HashMap<>();


    public void createRequestMap(Request request){

        this.requestListMap = request.getOrderItemsList()
                            .stream()
                            .collect(Collectors.toMap(
                                        OrderItem::getProductName, OrderItem::getQuantityNeeded));
    }

    public boolean isMoreThanZero(String productName) {
        return requestListMap.get(productName) > 0;
    }

    public int getProductQuantity(String productName) {
        return requestListMap.get(productName);
    }

    public void updateProductQuantity(String productName, int neededQuantity) {
        requestListMap.put(productName, neededQuantity);
    }


    /*public Map<String, Integer> getRequestList(Request request){

        return request.getOrderItemsList()
                .stream()
                .collect(Collectors.toMap(OrderItem::getProductName, OrderItem::getQuantityNeeded));
    }*/
}
