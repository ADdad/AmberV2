package amber_team.amber.dao.implementation;

import amber_team.amber.dao.interfaces.WarehouseDao;
import amber_team.amber.model.entities.Equipment;
import amber_team.amber.model.entities.Warehouse;
import amber_team.amber.util.SQLQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository(value = "warehouseDao")
public class WarehouseDaoImpl implements WarehouseDao {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Warehouse getById(String id){

        return  null;
    }

    @Override
    public List<Warehouse> getAll() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Warehouse> warehouseList = jdbcTemplate.query(
                SQLQueries.GET_ALL_WAREHOUSES,
                new RowMapper<Warehouse>() {
                    public Warehouse mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Warehouse c = new Warehouse();
                        c.setId(rs.getString(1));
                        c.setAdress(rs.getString(2));
                        c.setContactNumber(rs.getString(3));
                        return c;
                    }
                });
        return warehouseList;

    }


}
