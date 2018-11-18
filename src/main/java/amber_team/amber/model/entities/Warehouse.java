package amber_team.amber.model.entities;

import java.util.Map;

public class Warehouse {

    private String id;
    private String adress;
    private String contactNumber;
    private Map<Equipment, Integer> equipment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Map<Equipment, Integer> getEquipment() {
        return equipment;
    }

    public void setEquipment(Map<Equipment, Integer> equipment) {
        this.equipment = equipment;
    }
}
