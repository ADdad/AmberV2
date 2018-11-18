package amber_team.amber.model.dto;

import amber_team.amber.model.entities.AttrType;

import java.util.List;

public class AttributeDto {

    private String id;
    private String name;
    private int order;
    private String type;
    private boolean immutable;
    private boolean mandatory;
    private boolean multiple;
    private List<String> values;

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isImmutable() {
        return immutable;
    }

    public void setImmutable(boolean immutable) {
        this.immutable = immutable;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getValues() {
        return values;
    }

}
