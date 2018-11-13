package amber_team.amber.dao.interfaces;

import amber_team.amber.model.entities.Equipment;
import amber_team.amber.model.entities.Warehouse;

import javax.sql.DataSource;
import java.util.List;

public interface WarehouseDao {
    void setDataSource(DataSource dataSource);

    Warehouse getById(String id);

    List<Warehouse> getAll();
}
