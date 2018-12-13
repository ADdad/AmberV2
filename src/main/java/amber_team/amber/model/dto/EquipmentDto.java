package amber_team.amber.model.dto;

public class EquipmentDto {
    public EquipmentDto(String id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public EquipmentDto() {}

    private String id;
    private int quantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
