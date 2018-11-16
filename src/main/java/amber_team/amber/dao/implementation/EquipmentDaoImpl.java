package amber_team.amber.dao.implementation;

import amber_team.amber.dao.interfaces.EquipmentDao;
import amber_team.amber.model.dto.EquipmentSearchDto;
import amber_team.amber.model.entities.Equipment;
import amber_team.amber.util.SQLQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository(value = "equipmentDao")
public class EquipmentDaoImpl implements EquipmentDao {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Equipment getById(String id){

        return  null;
    }

    @Override
    public List<Equipment> getAll() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Equipment> equipment = jdbcTemplate.query(
                SQLQueries.GET_ALL_EQUIPMENT,
                new RowMapper<Equipment>() {
                    public Equipment mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Equipment c = new Equipment();
                        c.setId(rs.getString(1));
                        c.setModel(rs.getString(2));
                        c.setProducer(rs.getString(3));
                        c.setCountry(rs.getString(4));
                        return c;
                    }
                });
        return equipment;

    }

    @Override
    public List<Equipment> search(String value) {
        String formatValue = "%" + value + "%";
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Equipment> equipment = jdbcTemplate.query(
                SQLQueries.FIND_EQUIPMENT_BY_VALUE, new Object[]{formatValue, formatValue, formatValue},
                new RowMapper<Equipment>() {
                    public Equipment mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Equipment c = new Equipment();
                        c.setId(rs.getString(1));
                        c.setModel(rs.getString(2));
                        c.setProducer(rs.getString(3));
                        c.setCountry(rs.getString(4));
                        return c;
                    }
                });
        return equipment;

    }

    @Override
    public List<Equipment> getLimited(int limit) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Equipment> equipment = jdbcTemplate.query(
                SQLQueries.GET_LIMITED_EQUIPMENT, new Object[]{limit} ,
                new RowMapper<Equipment>() {
                    public Equipment mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Equipment c = new Equipment();
                        c.setId(rs.getString(1));
                        c.setModel(rs.getString(2));
                        c.setProducer(rs.getString(3));
                        c.setCountry(rs.getString(4));
                        return c;
                    }
                });
        return equipment;
    }


}
