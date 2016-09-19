package model.dto;

import lombok.Getter;
import lombok.Value;
import model.InventoryItem;
import model.OrderItem;
import model.ShippingMethod;
import model.Warehouse;
import strategy.model.Strategy;

import java.util.ArrayList;
import java.util.List;

@Getter
@Value
public class Request {
    private List<InventoryItem> inventoryItems;
    private List <Warehouse> warehouseList;
    private ShippingMethod shippingMethodMethod;
    private List<OrderItem> orderItemsList;
    private Strategy strategy;

    public Request(List<InventoryItem> inventoryItems,
                   List<Warehouse> warehouseList,
                   ShippingMethod shippingMethodMethod,
                   List<OrderItem> orderItemsList,
                   Strategy strategy) {

        this.inventoryItems = inventoryItems;
        this.warehouseList = warehouseList;
        this.shippingMethodMethod = shippingMethodMethod;
        this.orderItemsList = orderItemsList;
        this.strategy = strategy;
    }
}
