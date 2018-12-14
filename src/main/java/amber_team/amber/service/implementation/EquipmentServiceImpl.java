package amber_team.amber.service.implementation;

import amber_team.amber.dao.interfaces.EquipmentDao;
import amber_team.amber.dao.interfaces.WarehouseDao;
import amber_team.amber.model.dto.*;
import amber_team.amber.model.entities.Equipment;
import amber_team.amber.model.entities.Warehouse;
import amber_team.amber.service.interfaces.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<EquipmentDto> registeredEquipmentUnavalibleOnWarehouse =
                equipmentDao.getUnavailableEquipmentQuantity(requestId)
                .stream().filter(equip -> equip.getQuantity() > 0).collect(Collectors.toList());
        List<EquipmentInfoDto> requestEquipment = equipmentDao.getRequestEquipment(requestId);
        List<EquipmentDto> allUnavalibleEquipment = new ArrayList<>(registeredEquipmentUnavalibleOnWarehouse);
        for (EquipmentInfoDto equipInfo:
             requestEquipment) {
            if(findEquipmentById(registeredEquipmentUnavalibleOnWarehouse, equipInfo.getId()) == null){
                allUnavalibleEquipment.add(new EquipmentDto(equipInfo.getId(), equipInfo.getQuantity()));
            }
        }
        EquipmentListDto equipmentListDto = new EquipmentListDto();
        equipmentListDto.setList(allUnavalibleEquipment);
        return equipmentListDto;
    }

    private EquipmentDto findEquipmentById(List<EquipmentDto> listEquipment, String equipmentId) {
        return listEquipment.stream().filter(equip -> equipmentId.equals(equip.getId())).findFirst().orElse(null);
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
