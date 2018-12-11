package amber_team.amber.dao.implementation;

import amber_team.amber.dao.interfaces.EquipmentDao;
import amber_team.amber.model.dto.EquipmentDto;
import amber_team.amber.model.dto.EquipmentInfoDto;
import amber_team.amber.model.entities.Equipment;
import amber_team.amber.util.SQLQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository(value = "equipmentDao")
public class EquipmentDaoImpl implements EquipmentDao {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Equipment getById(String id) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        Equipment equipment = jdbcTemplate.queryForObject(
                SQLQueries.GET_EQUIPMENT_BY_ID, new Object[]{id},
                (rs, rowNum) -> getEquipment(rs));
        return equipment;
    }

    private Equipment getEquipment(ResultSet rs) throws SQLException {
        Equipment c = new Equipment();
        c.setId(rs.getString(1));
        c.setModel(rs.getString(2));
        c.setProducer(rs.getString(3));
        c.setCountry(rs.getString(4));
        return c;
    }

    @Override
    public List<Equipment> getAll() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Equipment> equipment = jdbcTemplate.query(
                SQLQueries.GET_ALL_EQUIPMENT,
                (rs, rowNum) -> getEquipment(rs));
        return equipment;

    }

    @Override
    public List<Equipment> search(String value) {
        String formatValue = "%" + value + "%";
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Equipment> equipment = jdbcTemplate.query(
                SQLQueries.FIND_EQUIPMENT_BY_VALUE, new Object[]{formatValue, formatValue, formatValue},
                (rs, rowNum) -> getEquipment(rs));
        return equipment;

    }

    @Override
    public ResponseEntity update(EquipmentDto e, String ware_id, int new_val) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String drop = SQLQueries.IF_EXISTS_EQUIP_ITS_QUANTITY;
        Integer cnt = jdbcTemplate.queryForObject(
                drop, Integer.class, e.getId(), ware_id);

        if (cnt != null && cnt > new_val) {
            jdbcTemplate.update(SQLQueries.UPDATE_WARE_EQUIP_QUANTITY, new_val, e.getId(), ware_id);
            return ResponseEntity.ok().body("Success");
        }
        return ResponseEntity.badRequest().body("Bad query");
    }


    @Override
    public List<Equipment> getLimited(int limit) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Equipment> equipment = jdbcTemplate.query(
                SQLQueries.GET_LIMITED_EQUIPMENT, new Object[]{limit},
                (rs, rowNum) -> getEquipment(rs));
        return equipment;
    }

    @Override
    public void create(Equipment newEquipment) {
        String id = UUID.randomUUID().toString();
        jdbcTemplate.update(SQLQueries.ADD_NEW_EQUIPMENT, id, newEquipment.getModel(), newEquipment.getProducer(), newEquipment.getCountry());

    }

    @Override
    public void removeEquipmentFromRequest(String requestId) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(SQLQueries.DELETE_REQUEST_EQUIPMENT, new Object[]{requestId});
    }

    @Override
    public void increaseEquipment(String requestId) {
        jdbcTemplate = new JdbcTemplate(dataSource);

        List<EquipmentInfoDto> equipment = getRequestEquipment(requestId);

        jdbcTemplate.batchUpdate(SQLQueries.UPDATE_EQUIPMENT_IN_WAREHOUSE_FROM_REQUEST, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                EquipmentInfoDto equipmentDto = equipment.get(i);
                ps.setInt(1, equipmentDto.getQuantity());
                ps.setString(2, equipmentDto.getId());
                ps.setString(3, requestId);
            }

            @Override
            public int getBatchSize() {
                return equipment.size();
            }
        });
    }

    @Override
    public List<EquipmentDto> decreaseEquipment(String requestId) {
        List<EquipmentDto> unavailableEquipment = getUnavailableEquipmentQuantity(requestId).parallelStream()
                .filter(equipmentDto -> equipmentDto.getQuantity() > 0)
                .collect(Collectors.toList());
        if (unavailableEquipment.isEmpty()) {
            jdbcTemplate = new JdbcTemplate(dataSource);

            List<EquipmentInfoDto> equipment = getRequestEquipment(requestId);

            jdbcTemplate.batchUpdate(SQLQueries.UPDATE_EQUIPMENT_IN_WAREHOUSE_FROM_REQUEST, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    EquipmentInfoDto equipmentDto = equipment.get(i);
                    ps.setInt(1, -equipmentDto.getQuantity());
                    ps.setString(2, equipmentDto.getId());
                    ps.setString(3, requestId);
                }

                @Override
                public int getBatchSize() {
                    return equipment.size();
                }
            });
        }
        return unavailableEquipment;
    }

    @Override
    public void addEquipmentToRequest(List<EquipmentDto> equipmentDtos, String requestId) {
        jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.batchUpdate(SQLQueries.ADD_REQUEST_EQUIPMENT, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                EquipmentDto equipmentDto = equipmentDtos.get(i);
                ps.setString(1, requestId);
                ps.setString(2, equipmentDto.getId());
                ps.setInt(3, equipmentDto.getQuantity());
            }

            @Override
            public int getBatchSize() {
                return equipmentDtos.size();
            }
        });
    }

    @Override
    public void addEquipmentToWarehouse(List<EquipmentDto> equipmentDtoList, String warehouseId) {
        jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.batchUpdate(SQLQueries.ADD_WAREHOUSE_EQUIPMENT, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                EquipmentDto equipmentDto = equipmentDtoList.get(i);
                ps.setString(1, warehouseId);
                ps.setString(2, equipmentDto.getId());
                ps.setInt(3, equipmentDto.getQuantity());
            }

            @Override
            public int getBatchSize() {
                return equipmentDtoList.size();
            }
        });
    }

    @Override
    public List<EquipmentInfoDto> getRequestEquipment(String requestId) {

        jdbcTemplate = new JdbcTemplate(dataSource);
        List<EquipmentInfoDto> equipment = jdbcTemplate.query(
                SQLQueries.GET_REQUEST_EQUIPMENT, new Object[]{requestId},
                new RowMapper<EquipmentInfoDto>() {
                    public EquipmentInfoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return getEquipmentInfoDto(rs);
                    }
                });
        return equipment;

    }

    @Override
    public List<EquipmentInfoDto> getWarehouseEquipment(String warehouseId) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(
                SQLQueries.EQUIPMENT_INFO_BY_WAREHOUSE, new Object[]{warehouseId},
                (rs, rowNum) -> getEquipmentInfoDto(rs));
    }

    private EquipmentInfoDto getEquipmentInfoDto(ResultSet rs) throws SQLException {
        EquipmentInfoDto c = new EquipmentInfoDto();
        c.setId(rs.getString("id"));
        c.setModel(rs.getString("model"));
        c.setProducer(rs.getString("producer"));
        c.setCountry(rs.getString("country"));
        c.setQuantity(rs.getInt("quantity"));
        return c;
    }

    @Override
    public List<EquipmentDto> getUnavailableEquipmentQuantity(String requestId) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<EquipmentDto> equipment = jdbcTemplate.query(
                SQLQueries.GET_REQUEST_WAREHOUSE_EQUIPMENT_QUANTITY_DIFFERENCE, new Object[]{requestId},
                new RowMapper<EquipmentDto>() {
                    public EquipmentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        EquipmentDto c = new EquipmentDto();
                        c.setId(rs.getString("id"));
                        c.setQuantity(rs.getInt("quantity_diff"));
                        return c;
                    }
                });
        return equipment;
    }


}
