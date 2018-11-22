package amber_team.amber.model.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public class Request {

    private String id;
    private String warehouseId;
    private String creatorId;
    private String executorId;
    private String typeId;
    private String title;
    private String status;
    private String connectedRequestId;
    private Timestamp creationDate;
    private Timestamp modifiedDate;
    private String description;
    private boolean archive;
    private List<Attribute> attributes;
    private Map<Equipment, Integer> equipment;
    private List<Comment> comments;

    public String getConnectedRequestId() {
        return connectedRequestId;
    }

    public void setConnectedRequestId(String connectedRequestId) {
        this.connectedRequestId = connectedRequestId;
    }

    public String getId() {
        return id;
    }

    public void setId(String requestId) {
        this.id = requestId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public Map<Equipment, Integer> getEquipment() {
        return equipment;
    }

    public void setEquipment(Map<Equipment, Integer> equipment) {
        this.equipment = equipment;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}