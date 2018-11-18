package amber_team.amber.model.entities;

import java.time.LocalDateTime;
import java.util.Map;

public class Type {

    private String id;
    private String name;
    private LocalDateTime creationDate;
    private Map<AttrType, TypeProperties> attrTypes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Map<AttrType, TypeProperties> getAttrTypes() {
        return attrTypes;
    }

    public void setAttrTypes(Map<AttrType, TypeProperties> attrTypes) {
        this.attrTypes = attrTypes;
    }
}
