package amber_team.amber.model.dto;

import java.util.List;

public class RequestSaveDto {

    private String creatorId;
    private String title;
    private String description;
    private String type;
    private List<AttributeSaveDto> attributes;
    private List<EquipmentDto> items;
    private String warehouseId;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AttributeSaveDto> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeSaveDto> attributes) {

        this.attributes = attributes;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<EquipmentDto> getItems() {
        return items;
    }

    public void setItems(List<EquipmentDto> items) {
        this.items = items;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

}
