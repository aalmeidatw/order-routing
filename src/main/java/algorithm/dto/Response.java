package algorithm.dto;

import lombok.Getter;
import lombok.Value;
import model.InventoryItem;
import java.util.List;

@Getter
@Value
public class Response {
    private List<InventoryItem> inventoryItems;

    public Response(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }
}
