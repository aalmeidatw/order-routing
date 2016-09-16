package strategy.model;


import model.InventoryItem;
import model.Warehouse;

import java.util.List;

public interface Strategy {
    List<InventoryItem> execute(List<InventoryItem> inventoryItems);
}
