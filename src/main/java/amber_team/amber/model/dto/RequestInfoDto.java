package amber_team.amber.model.dto;

import amber_team.amber.model.entities.*;
import amber_team.amber.util.Status;

import java.sql.Timestamp;
import java.util.List;

public class RequestInfoDto {

    private String id;
    private Warehouse warehouse;
    private User creator;
    private User executor;
    private Type type;
    private String title;
   // private String status;
    private Timestamp creationDate;
    private Timestamp modifiedDate;
    private String description;
    private boolean archive;
    private List<AttributeInfoDto> attributes;
    private List<Comment> comments;
    private List<EquipmentInfoDto> equipment;
    private String connectedRequest;
    private Status status;

    public RequestInfoDto() {
    }

    public RequestInfoDto(Request request) {
        this.id = request.getId();
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.archive = request.isArchive();
        this.creationDate = request.getCreationDate();
        this.modifiedDate = request.getModifiedDate();
        this.status = request.getStatus();
        if (request.getConnectedRequestId() != null) this.connectedRequest = request.getConnectedRequestId();
    }

    public void setStatus(String status) {
        this.status = Status.valueOfStatus(status);
    }

    public Status getStatus() {
        return status;
    }

    public String getConnectedRequest() {
        return connectedRequest;
    }

    public void setConnectedRequest(String connectedRequest) {
        this.connectedRequest = connectedRequest;
    }

    public List<EquipmentInfoDto> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<EquipmentInfoDto> equipment) {
        this.equipment = equipment;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public List<AttributeInfoDto> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeInfoDto> attributes) {
        this.attributes = attributes;
    }
}
