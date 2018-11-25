package amber_team.amber.model.dto;

public class ReportEndingEquipmentDto extends ReportAvailableEquipmentDto {
    public int getEquipmentThreshold() {
        return equipmentThreshold;
    }

    public void setEquipmentThreshold(int equipmentThreshold) {
        this.equipmentThreshold = equipmentThreshold;
    }

    private int equipmentThreshold;

}
