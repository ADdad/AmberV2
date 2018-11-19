package amber_team.amber.dao.interfaces;

import amber_team.amber.model.dto.EquipmentInfoDto;
import amber_team.amber.model.dto.EquipmentDto;
import amber_team.amber.model.entities.Equipment;

import java.util.List;

public interface EquipmentDao {

    Equipment getById(String id);

    List<Equipment> getAll();
    List<Equipment> search(String value);
    List<Equipment> getLimited(int limit);
    void addEquipmentToRequest(List<EquipmentDto> equipmentDtos, String request_id);
    void addEquipmentToWarehouse(List<EquipmentDto> equipmentDtoList, String warehouse_id);
    List<EquipmentInfoDto> getRequestEquipment(String requestId);
    List<EquipmentInfoDto> getWarehouseEquipment(String warehouseId);

}
