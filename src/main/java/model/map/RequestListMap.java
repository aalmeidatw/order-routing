package model.map;

import model.OrderItem;
import model.dto.Request;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestListMap {

    public Map<String, Integer> getRequestList(Request request){

        return request.getOrderItemsList()
                .stream()
                .collect(Collectors.toMap(OrderItem::getProductName, OrderItem::getQuantityNeeded));
    }
}
