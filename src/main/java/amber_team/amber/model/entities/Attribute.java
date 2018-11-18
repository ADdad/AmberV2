package amber_team.amber.model.entities;

import java.util.Map;

public class Attribute<ValueType> {

    private String id;
    private String name;
    private AttrType attrType;
    private Request request;
    private Map<String, ValueType> values;

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

    public AttrType getAttrType() {
        return attrType;
    }

    public void setAttrType(AttrType attrType) {
        this.attrType = attrType;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Map<String, ValueType> getValues() {
        return values;
    }

    public void setValues(Map<String, ValueType> values) {
        this.values = values;
    }
}
