package amber_team.amber.model.dto;

public class ReportEquipmentResponseDto {
    private String equipmentModel;
    private String equipmentProducer;
    private String equipmentCountry;
    private int quantity;

    public String getEquipmentModel() {
        return equipmentModel;
    }

    public void setEquipmentModel(String equipmentModel) {
        this.equipmentModel = equipmentModel;
    }

    public String getEquipmentProducer() {
        return equipmentProducer;
    }

    public void setEquipmentProducer(String equipmentProducer) {
        this.equipmentProducer = equipmentProducer;
    }

    public String getEquipmentCountry() {
        return equipmentCountry;
    }

    public void setEquipmentCountry(String equipmentCountry) {
        this.equipmentCountry = equipmentCountry;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
