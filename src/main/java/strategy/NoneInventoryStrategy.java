package strategy;


import model.InventoryItem;
import strategy.model.Strategy;

import java.util.List;

public class NoneInventoryStrategy implements Strategy {

    @Override
    public List<InventoryItem> executeStrategy(List<InventoryItem> inventoryItems) {
        return inventoryItems;
    }
}
