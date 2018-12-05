package amber_team.amber.model.dto;

import java.util.List;

public class RequestSaveDto {

    private String requestId;
    private String creatorId;
    private String title;
    private String description;
    private String type;
    private List<AttributeInfoDto> attributes;
    private List<EquipmentDto> items;
    private String warehouseId;
    private int offset;
    private String connectedRequest;

    public String getConnectedRequest() {
        return connectedRequest;
    }

    public void setConnectedRequest(String connectedRequest) {
        this.connectedRequest = connectedRequest;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

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

    public List<AttributeInfoDto> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeInfoDto> attributes) {

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
