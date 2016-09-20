package model.Map;


import model.Warehouse;
import model.dto.Request;
import java.util.Map;
import java.util.stream.Collectors;

public class CapacityListMap {

    public Map<String, Integer> getCapacityList(Request request){
        return request.getWarehouseList()
                .stream()
                .collect(Collectors.toMap(Warehouse::getWarehouseName, Warehouse::getCapacity));
    }
}
