package amber_team.amber.dao.implementation;


import amber_team.amber.dao.interfaces.RequestDao;
import amber_team.amber.model.dto.AttributeDto;
import amber_team.amber.model.dto.RequestStatusChangeDto;
import amber_team.amber.model.entities.Request;
import amber_team.amber.util.MergeReflectionUtil;
import amber_team.amber.util.SQLQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
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
    private static final Logger log = LoggerFactory.getLogger(RequestDaoImpl.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public Request create(Request request) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String id = UUID.randomUUID().toString();

        String status = request.getStatus();
        if (status == null)
            status = "Opened";

        Timestamp creationDate = Timestamp.valueOf(LocalDateTime.now());

        jdbcTemplate.update(SQLQueries.ADD_NEW_REQUEST, id, request.getCreatorId(), request.getTypeId(), status,
                creationDate, creationDate, request.getDescription(), false, request.getWarehouseId(), request.getTitle());

        request.setId(id);

        return request;
    }

    @Override
    public Request update(Request request){
        Request oldRequest = this.getById(request);
        jdbcTemplate = new JdbcTemplate(dataSource);
        Request newRequest = null;
        try {
            newRequest = MergeReflectionUtil.mergeObjects(request, oldRequest);
            newRequest.setModifiedDate(Timestamp.valueOf(LocalDateTime.now()));
            jdbcTemplate.update(SQLQueries.UPDATE_REQUEST, newRequest.getWarehouseId(), newRequest.getCreatorId(), newRequest.getExecutorId(), newRequest.getTypeId(), newRequest.getConnectedRequestId(), newRequest.getTitle(), newRequest.getStatus(), newRequest.getCreationDate(), newRequest.getModifiedDate(), newRequest.getDescription(), newRequest.isArchive(), newRequest.getId());
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return newRequest;
    }




    @Override
    public Request getById(Request request) {

        jdbcTemplate = new JdbcTemplate(dataSource);
        Request info = jdbcTemplate.queryForObject(SQLQueries.REQUEST_INFO_BY_ID, new Object[] {request.getId()}, new RowMapper<Request>() {
            @Override
            public Request mapRow(ResultSet resultSet, int i) throws SQLException {
                Request info = new Request();
                info.setId(request.getId());
                info.setWarehouseId(resultSet.getString("warehouse_id"));
                info.setCreatorId(resultSet.getString("creator_id"));
                info.setExecutorId(resultSet.getString("executor_id"));
                info.setTypeId(resultSet.getString("req_type_id"));
                info.setTitle(resultSet.getString("title"));
                info.setStatus(resultSet.getString("status"));
                info.setConnectedRequestId(resultSet.getString("connected_request"));
                info.setCreationDate(resultSet.getTimestamp("creation_date"));
                info.setModifiedDate(resultSet.getTimestamp("modified_date"));
                info.setDescription(resultSet.getString("description"));
                info.setArchive(resultSet.getBoolean("archive"));
                return info;
            }
        });

        return info;
    }

    public Request getById(String id) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.queryForObject(SQLQueries.REQUEST_INFO_BY_ID, new Object[] {id}, new RowMapper<Request>() {
            @Override
            public Request mapRow(ResultSet resultSet, int i) throws SQLException {
                Request info = new Request();
                info.setId(id);
                info.setWarehouseId(resultSet.getString("warehouse_id"));
                info.setCreatorId(resultSet.getString("creator_id"));
                info.setExecutorId(resultSet.getString("executor_id"));
                info.setTypeId(resultSet.getString("req_type_id"));
                info.setTitle(resultSet.getString("title"));
                info.setStatus(resultSet.getString("status"));
                info.setConnectedRequestId(resultSet.getString("connected_request"));
                info.setCreationDate(resultSet.getTimestamp("creation_date"));
                info.setModifiedDate(resultSet.getTimestamp("modified_date"));
                info.setDescription(resultSet.getString("description"));
                info.setArchive(resultSet.getBoolean("archive"));
                return info;
            }
        });
    }

    @Override
    public void archiveOldRequests() {
        log.info("Archiving old requests for {}", dateFormat.format(new Date()));
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(SQLQueries.ARCHIVE_OLD_REQUESTS);
        log.info("Archiving ended on {}", dateFormat.format(new Date()));
    }
}
