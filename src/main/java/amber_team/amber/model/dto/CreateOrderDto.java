package amber_team.amber.model.dto;

import amber_team.amber.model.entities.Equipment;
import amber_team.amber.model.entities.Warehouse;

import java.util.List;

public class CreateOrderDto {
    public CreateOrderDto(List<AttributeDto> attributes) {
        this.attributes = attributes;
    }

    private List<AttributeDto> attributes;
    private List<Warehouse> warehouses;
    private List<Equipment> equipment;

    public CreateOrderDto() {
    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }

    public List<AttributeDto> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeDto> attributes) {
        this.attributes = attributes;
    }
}
