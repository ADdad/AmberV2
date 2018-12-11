package amber_team.amber.dao.interfaces;

import amber_team.amber.model.dto.EquipmentInfoDto;
import amber_team.amber.model.dto.EquipmentDto;
import amber_team.amber.model.dto.MyRequestStatusChangeDto;
import amber_team.amber.model.entities.Equipment;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EquipmentDao {

    Equipment getById(String id);

    List<Equipment> getAll();

    List<Equipment> search(String value);

    List<Equipment> getLimited(int limit);

    void create(Equipment newEquipment);

    public ResponseEntity update(EquipmentDto e, String ware_id, int new_val);

    void addEquipmentToRequest(List<EquipmentDto> equipmentDtos, String request_id);

    void addEquipmentToWarehouse(List<EquipmentDto> equipmentDtoList, String warehouse_id);

    List<EquipmentInfoDto> getRequestEquipment(String requestId);

    List<EquipmentInfoDto> getWarehouseEquipment(String warehouseId);

    List<EquipmentDto> getUnavailableEquipmentQuantity(String requestId);

    void removeEquipmentFromRequest(String requestId);

    void increaseEquipment(String requestId);

    List<EquipmentDto> decreaseEquipment(String requestId);
}
