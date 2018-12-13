package amber_team.amber.service.interfaces;

import amber_team.amber.model.dto.EquipmentAddingDto;
import amber_team.amber.model.dto.EquipmentListDto;
import amber_team.amber.model.dto.EquipmentSearchDto;
import amber_team.amber.model.dto.WarehouseListDto;
import amber_team.amber.model.entities.Equipment;
import amber_team.amber.model.entities.Warehouse;

import java.util.List;

public interface EquipmentService {
    void addEquipmentToWarehouse(EquipmentAddingDto equipmentAddingDto);

    EquipmentSearchDto getLimitedEquipment();

    EquipmentListDto unavailableEquipmentByRequestId(String requestId);

    List<Warehouse> getWarehouses();

    void addNewEquipment(Equipment equipment);
}
