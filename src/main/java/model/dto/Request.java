package model.dto;

import lombok.Getter;
import model.InventoryItem;
import model.ShippingMethod;
import model.Warehouse;
import strategy.model.Strategy;

import java.util.List;

@Getter
public class Request {
    private List<InventoryItem> inventoryItems;
    private List <Warehouse> warehouseList;
    private ShippingMethod shippingMethodMethod;
    private Strategy strategy;
}
