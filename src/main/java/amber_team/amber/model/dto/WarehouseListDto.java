package amber_team.amber.model.dto;

import amber_team.amber.model.entities.Warehouse;

import java.util.List;

public class WarehouseListDto {

    private List<Warehouse> warehouses;

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }
}
