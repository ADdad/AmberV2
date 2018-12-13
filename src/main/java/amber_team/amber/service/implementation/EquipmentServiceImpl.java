package amber_team.amber.service.implementation;

import amber_team.amber.dao.interfaces.EquipmentDao;
import amber_team.amber.dao.interfaces.WarehouseDao;
import amber_team.amber.model.dto.EquipmentAddingDto;
import amber_team.amber.model.dto.EquipmentListDto;
import amber_team.amber.model.dto.EquipmentSearchDto;
import amber_team.amber.model.entities.Equipment;
import amber_team.amber.model.entities.Warehouse;
import amber_team.amber.service.interfaces.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service(value = "equipmentService")
public class EquipmentServiceImpl implements EquipmentService {
    private static final int EQUIPMENT_LIMIT = 100;

    private final EquipmentDao equipmentDao;
    private final WarehouseDao warehouseDao;


    @Autowired
    public EquipmentServiceImpl(EquipmentDao equipmentDao, WarehouseDao warehouseDao) {
        this.equipmentDao = equipmentDao;
        this.warehouseDao = warehouseDao;
    }

    @Override
    public void addEquipmentToWarehouse(EquipmentAddingDto equipmentAddingDto) {
        equipmentDao.addEquipmentToWarehouse(equipmentAddingDto.getItems(), equipmentAddingDto.getWarehouseId());
    }

    @Override
    public EquipmentSearchDto getLimitedEquipment() {
        List<Equipment> equipment = equipmentDao.getLimited(EQUIPMENT_LIMIT);
        EquipmentSearchDto equipmentSearchDto = new EquipmentSearchDto();
        equipmentSearchDto.setEquipment(equipment);
        return equipmentSearchDto;
    }

    @Override
    public EquipmentListDto unavailableEquipmentByRequestId(String requestId) {
        EquipmentListDto equipmentListDto = new EquipmentListDto();
        equipmentListDto.setList(equipmentDao.getUnavailableEquipmentQuantity(requestId)
                .stream().filter(equip -> equip.getQuantity() > 0).collect(Collectors.toList()));
        return equipmentListDto;
    }

    @Override
    public List<Warehouse> getWarehouses() {
        return warehouseDao.getAll();
    }

    @Override
    public void addNewEquipment(Equipment equipment) {
        equipmentDao.create(equipment);
    }
}
