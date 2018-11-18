package amber_team.amber.model.dto;

import java.sql.Timestamp;
import java.util.List;

public class RequestInfoDto {

    private String username;
    private String id;
    private String warehouse_id;
    private String creator_id;
    private String executor_id;
    private String type_id;
    private String title;
    private String status;
    private Timestamp creation_date;
    private Timestamp modified_date;
    private String description;
    private boolean archive;
    private List<AttributeDto> attributes;

    public Timestamp getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Timestamp creation_date) {
        this.creation_date = creation_date;
    }

    public Timestamp getModified_date() {
        return modified_date;
    }

    public void setModified_date(Timestamp modified_date) {
        this.modified_date = modified_date;
    }

    public void setAttributes(List<AttributeDto> attributes) {
        this.attributes = attributes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(String warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getExecutor_id() {
        return executor_id;
    }

    public void setExecutor_id(String executor_id) {
        this.executor_id = executor_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
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


}
