package amber_team.amber.dao.implementation;


import amber_team.amber.dao.interfaces.RequestDao;
import amber_team.amber.model.dto.AttributeDto;
import amber_team.amber.model.entities.Request;
import amber_team.amber.model.dto.RequestInfoDto;
import amber_team.amber.model.dto.RequestStatusChangeDto;
import amber_team.amber.util.SQLQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

@Repository(value = "requestDao")
public class RequestDaoImpl implements RequestDao {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Request save(Request request) {
        jdbcTemplate = new JdbcTemplate(dataSource);

        String id = UUID.randomUUID().toString();

        String status = request.getStatus();
        if (status == null)
            status = "Opened";

        Timestamp creationDate = Timestamp.valueOf(LocalDateTime.now());

        jdbcTemplate.update(SQLQueries.ADD_NEW_REQUEST, id, request.getCreatorId(), request.getTypeId(), status,
                creationDate, creationDate, request.getDescription(), false, request.getWarehouseId());

        Request result = new Request();
        result.setId(id);
        result.setCreatorId(request.getCreatorId());
        result.setTypeId(request.getTypeId());
        result.setStatus(status);
        result.setCreationDate(creationDate);
        result.setModifiedDate(creationDate);
        result.setDescription(request.getDescription());
        result.setArchive(false);

        return result;
    }

    @Override
    public ResponseEntity open(RequestStatusChangeDto request) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(SQLQueries.CHANGE_REQUEST_STATUS_AND_CREATOR_ID, request.getCreatorId(), request.getStatus(),
                LocalDate.now(), request.getRequestId());
        return ResponseEntity.ok(request);
    }

    @Override
    public ResponseEntity cancel(RequestStatusChangeDto request) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(SQLQueries.CHANGE_REQUEST_STATUS, request.getStatus(), LocalDate.now(), request.getRequestId());
        return ResponseEntity.ok(request);
    }

    @Override
    public ResponseEntity review(RequestStatusChangeDto request) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(SQLQueries.CHANGE_REQUEST_STATUS, request.getStatus(), LocalDate.now(), request.getRequestId());
        return ResponseEntity.ok(request);
    }

    @Override
    public ResponseEntity reject(RequestStatusChangeDto request) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(SQLQueries.CHANGE_REQUEST_STATUS, request.getStatus(), LocalDate.now(), request.getRequestId());
        return ResponseEntity.ok(request);
    }

    @Override
    public ResponseEntity progress(RequestStatusChangeDto request) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(SQLQueries.CHANGE_REQUEST_STATUS_AND_EXECUTOR_ID, request.getStatus(), request.getExecutorId(),
                LocalDate.now(), request.getRequestId());
        return ResponseEntity.ok(request);
    }

    @Override
    public ResponseEntity hold(RequestStatusChangeDto request) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(SQLQueries.CHANGE_REQUEST_STATUS, request.getStatus(), LocalDate.now(), request.getRequestId());
        return ResponseEntity.ok(request);
    }

    @Override
    public ResponseEntity deliver(RequestStatusChangeDto request) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(SQLQueries.CHANGE_REQUEST_STATUS, request.getStatus(), LocalDate.now(), request.getRequestId());
        return ResponseEntity.ok(request);
    }

    @Override
    public ResponseEntity complete(RequestStatusChangeDto request) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(SQLQueries.CHANGE_REQUEST_STATUS, request.getStatus(), LocalDate.now(), request.getRequestId());
        return ResponseEntity.ok(request);
    }

    @Override
    public List<AttributeDto> attributes(String type) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<AttributeDto> attributeDtoList = jdbcTemplate.query(
                SQLQueries.REQUEST_ATTRIBUTES_BY_TYPE,
                new Object[] {type},
                new RowMapper<AttributeDto>() {
                    public AttributeDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        AttributeDto c = new AttributeDto();
                        c.setId(rs.getString(1));
                        c.setName(rs.getString(2));
                        c.setType(rs.getString(3));
                        c.setOrder(rs.getInt(4));
                        c.setMultiple(rs.getBoolean(5));
                        c.setMandatory(rs.getBoolean(6));
                        c.setImmutable(rs.getBoolean(7));
                        return c;
                    }
                });
        for (AttributeDto attr:
             attributeDtoList) {
            getAttributeValues(attr, jdbcTemplate);
        }
        return attributeDtoList;
    }

    public void getAttributeValues(AttributeDto attr, JdbcTemplate jdbcTemplate){
        List<String> values = jdbcTemplate.query(SQLQueries.RESERVED_VALUES_FOR_ATTRIBUTE_ID, new Object[] {attr.getId()} , new RowMapper<String>(){
            public String mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getString(1);
            }
        });
        attr.setValues(values);
    }


    @Override
    public ResponseEntity getRequestInfo(RequestStatusChangeDto request) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        RequestInfoDto info = jdbcTemplate.queryForObject(SQLQueries.REQUEST_INFO_BY_ID, new Object[] {request.getRequestId()}, new RowMapper<RequestInfoDto>() {
            @Override
            public RequestInfoDto mapRow(ResultSet resultSet, int i) throws SQLException {
                RequestInfoDto info = new RequestInfoDto();
                info.setWarehouse_id(resultSet.getString("warehouse_id"));
                info.setExecutor_id(resultSet.getString("executor_id"));
                info.setType_id(resultSet.getString(" type_id"));
                info.setTitle(resultSet.getString("title"));
                info.setStatus(resultSet.getString("status"));
                info.setCreation_date(resultSet.getDate("creation_date").toLocalDate());
                info.setModified_date(resultSet.getDate("modified_date").toLocalDate());
                info.setDescription(resultSet.getString("description"));
                info.setArchive(resultSet.getBoolean("archive"));
                return info;
            }
        });

        List<String> attributes = jdbcTemplate.queryForList(SQLQueries.REQUEST_ATTRIBUTES_BY_ID, new Object[] {info.getRequest_id()}, String.class);
        info.setAttributes(attributes);
        return ResponseEntity.ok(info);
    }
}
