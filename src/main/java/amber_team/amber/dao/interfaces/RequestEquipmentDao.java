package amber_team.amber.dao.interfaces;

import amber_team.amber.model.dto.EquipmentDto;
import amber_team.amber.model.entities.Equipment;

public interface RequestEquipmentDao {

    public void save(EquipmentDto equipmentDto, String request_id);


}
