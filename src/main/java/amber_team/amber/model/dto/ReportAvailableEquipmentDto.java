package amber_team.amber.model.dto;

public class ReportAvailableEquipmentDto extends PaginationDto {
    private String warehouseId;

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }
}
