package amber_team.amber.dao.implementation;

import amber_team.amber.dao.interfaces.EquipmentDao;
import amber_team.amber.dao.interfaces.RequestEquipmentDao;
import amber_team.amber.model.dto.EquipmentDto;
import amber_team.amber.model.entities.Equipment;
import amber_team.amber.util.SQLQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
@Repository(value = "requestEquipmentDao")
public class RequestEquipmentDaoImpl implements RequestEquipmentDao {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void save(EquipmentDto equipmentDto, String request_id) {
        jdbcTemplate.update(SQLQueries.ADD_REQUEST_EQUIPMENT, request_id, equipmentDto.getId(), equipmentDto.getQuantity());
    }
}
