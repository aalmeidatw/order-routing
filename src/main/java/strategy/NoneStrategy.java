package strategy;


import model.InventoryItem;
import strategy.model.Strategy;

import java.util.List;

public class NoneStrategy implements Strategy {

    @Override
    public List<InventoryItem> executeStrategy(List<InventoryItem> inventoryItems) {
        return inventoryItems;
    }
}
