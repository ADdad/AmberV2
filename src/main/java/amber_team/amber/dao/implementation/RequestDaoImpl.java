package amber_team.amber.dao.implementation;


import amber_team.amber.dao.interfaces.RequestDao;
import amber_team.amber.model.entities.Request;
import amber_team.amber.model.dto.RequestInfoDto;
import amber_team.amber.model.dto.RequestStatusChangeDto;
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

    public ResponseEntity save(Request request) {
        jdbcTemplate = new JdbcTemplate(dataSource);

        String id = UUID.randomUUID().toString();

        String status = request.getStatus();
        if (status == null)
            status = "Created";

        LocalDateTime creationDate = LocalDateTime.now();

        jdbcTemplate.update(SQLQueries.ADD_NEW_REQUEST, id, request.getCreatorId(), request.getTypeId(), status,
                creationDate, creationDate, request.getDescription(), 0);

        Request result = new Request();
        result.setId(id);
        result.setCreatorId(request.getCreatorId());
        result.setTypeId(request.getTypeId());
        result.setStatus(status);
        result.setCreationDate(creationDate);
        result.setModifiedDate(creationDate);
        result.setDescription(request.getDescription());
        result.setArchive(false);

        return ResponseEntity.ok(result);
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

    @Override
    public void archiveOldRequests(Date tenDaysBefore) {
        log.info("Archiving old requests for {}", dateFormat.format(new Date()));
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(SQLQueries.ARCHIVE_OLD_REQUESTS, new Object[] {tenDaysBefore});
        log.info("Archiving ended on {}", dateFormat.format(new Date()));
    }
}
