package amber_team.amber.dao.implementation;

import amber_team.amber.dao.interfaces.WarehouseDao;
import amber_team.amber.model.dto.UserInfoDto;
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
    public Warehouse getById(String id) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        Warehouse warehouse = jdbcTemplate.queryForObject(SQLQueries.GET_WAREHOUSE_BY_ID, new Object[]{id}, new RowMapper<Warehouse>() {
            @Override
            public Warehouse mapRow(ResultSet resultSet, int i) throws SQLException {
                Warehouse info = new Warehouse();
                info.setId(resultSet.getString("id"));
                info.setAdress(resultSet.getString("adress"));
                info.setContactNumber(resultSet.getString("contact_number"));
                return info;
            }
        });

        return warehouse;
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

    @Override
    public List<UserInfoDto> getExecutors(String warehouseId) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<UserInfoDto> userInfoDtos = jdbcTemplate.query(
                SQLQueries.GET_WAREHOUSE_EXECUTORS, new Object[]{warehouseId},
                new RowMapper<UserInfoDto>() {
                    public UserInfoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        UserInfoDto c = new UserInfoDto();
                        c.setId(rs.getString("id"));
                        c.setFirstName(rs.getString("f_name"));
                        c.setSecondName(rs.getString("s_name"));
                        c.setEmail(rs.getString("email"));
                        return c;
                    }
                });
        return userInfoDtos;
    }


}
