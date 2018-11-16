package amber_team.amber.model.dto;

import amber_team.amber.model.entities.Equipment;

import java.util.List;

public class EquipmentSearchDto {
    private List<Equipment> equipment;
    private boolean hasMore;

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }
}
