package model.dto;

import lombok.Getter;
import lombok.Value;
import model.InventoryItem;
import model.WarehouseFulfill;

import java.util.List;

@Getter
@Value
public class Response {
    private List<WarehouseFulfill> warehouseFulfills;

    public Response(List<WarehouseFulfill> warehouseFulfills) {
        this.warehouseFulfills = warehouseFulfills;
    }
}
