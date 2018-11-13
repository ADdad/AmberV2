package amber_team.amber.dao.interfaces;

import amber_team.amber.model.entities.Equipment;
import amber_team.amber.model.entities.Warehouse;

import javax.sql.DataSource;
import java.util.List;

public interface EquipmentDao {
    void setDataSource(DataSource dataSource);

    Equipment getById(String id);

    List<Equipment> getAll();
}
