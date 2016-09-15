package model.dto;

import model.InventoryItem;
import model.Shipping;
import model.Warehouse;

import java.util.List;

public class Request {
    private List<InventoryItem> inventoryItems;
    private List <Warehouse> warehouseList;
    private Shipping shippingMethod;
}
