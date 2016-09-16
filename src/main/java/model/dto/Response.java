package model.dto;


import lombok.Getter;
import model.InventoryItem;

import java.util.List;


public class Response {
    @Getter private List<InventoryItem> inventoryItems;

    public Response(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }


}
