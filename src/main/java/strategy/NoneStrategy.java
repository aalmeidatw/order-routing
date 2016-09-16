package strategy;


import model.InventoryItem;
import strategy.model.Strategy;

import java.util.List;

public class NoneStrategy implements Strategy {

    @Override
    public List<InventoryItem> execute(List<InventoryItem> inventoryItems) {
        return inventoryItems;
    }
}
