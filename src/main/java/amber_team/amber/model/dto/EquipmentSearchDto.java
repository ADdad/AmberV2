package amber_team.amber.model.dto;

import amber_team.amber.model.entities.Equipment;

import java.util.List;

public class EquipmentSearchDto {
    private List<Equipment> equipment;

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }
}
