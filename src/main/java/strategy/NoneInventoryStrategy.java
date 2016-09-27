package strategy;


import model.InventoryItem;
import model.Warehouse;
import strategy.model.Strategy;
import java.util.List;

public class NoneInventoryStrategy implements Strategy {

    @Override
    public List<InventoryItem> executeStrategy(List<InventoryItem> inventoryItems, List<Warehouse> warehouseList) {
        return inventoryItems;
    }
}
