package amber_team.amber.model.dto;

import java.util.List;

public class EquipmentAddingDto {

    private String warehouseId;

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    private List<EquipmentDto> items;

    public List<EquipmentDto> getItems() {
        return items;
    }

    public void setItems(List<EquipmentDto> items) {
        this.items = items;
    }
}
