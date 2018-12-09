package amber_team.amber.dao.implementation;


import amber_team.amber.dao.interfaces.RequestDao;
import amber_team.amber.model.entities.Request;
import amber_team.amber.util.MergeReflectionUtil;
import amber_team.amber.util.SQLQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository(value = "requestDao")
public class RequestDaoImpl implements RequestDao {
    private static final Logger log = LoggerFactory.getLogger(RequestDaoImpl.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RequestDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

//    public void setDataSource(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }

    public Request create(Request request) {
        String id = UUID.randomUUID().toString();

        String status = request.getStatus();
        if (status == null)
            status = "Opened";

        Timestamp creationDate = Timestamp.valueOf(LocalDateTime.now());

        jdbcTemplate.update(SQLQueries.ADD_NEW_REQUEST, id, request.getCreatorId(), request.getTypeId(), status,
                creationDate, creationDate, request.getDescription(), false, request.getWarehouseId(), request.getTitle());
        if (request.getConnectedRequestId() != null) {
            jdbcTemplate.update(SQLQueries.CONNECT_REQUEST, request.getConnectedRequestId(), id);
        }
        request.setId(id);

        return request;
    }

    @Override
    public Request update(Request request) {
        Request oldRequest = this.getByRequest(request);
        Request newRequest = null;
        try {
            newRequest = MergeReflectionUtil.mergeObjects(request, oldRequest);
            newRequest.setModifiedDate(Timestamp.valueOf(LocalDateTime.now()));
            jdbcTemplate.update(SQLQueries.UPDATE_REQUEST, newRequest.getWarehouseId(), newRequest.getCreatorId(),
                    newRequest.getExecutorId(), newRequest.getTypeId(), newRequest.getConnectedRequestId(),
                    newRequest.getTitle(), newRequest.getStatus(), newRequest.getCreationDate(),
                    newRequest.getModifiedDate(), newRequest.getDescription(), newRequest.isArchive(), newRequest.getId());
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return newRequest;
    }


    @Override
    public Request getByRequest(Request request) {
        return getById(request.getId());
    }

    @Override
    public Request getById(String requestId) {
        Request info = jdbcTemplate.queryForObject(SQLQueries.REQUEST_INFO_BY_ID, new Object[]{requestId}, new RowMapper<Request>() {
            @Override
            public Request mapRow(ResultSet resultSet, int i) throws SQLException {
                return mapRequest(resultSet);
            }
        });

        return info;
    }

    @Override
    public int getCountOfUsersActiveRequests(String userId) {
        Integer count = jdbcTemplate.queryForObject(SQLQueries.USERS_ACTIVE_REQUESTS_COUNT, new Object[]{userId}, Integer.class);
        if (count == null) return 0;
        return count;
    }

    @Override
    public List<Request> getAllUsersRequests(String userId) {
        List<Request> requests = jdbcTemplate.query(
                SQLQueries.GET_USERS_CREATED_REQUESTS, new Object[]{userId},
                new RowMapper<Request>() {
                    public Request mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                        return mapRequest(resultSet);
                    }
                });
        return requests;
    }

    private Request mapRequest(ResultSet resultSet) throws SQLException {
        Request info = new Request();
        info.setId(resultSet.getString("id"));
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

    @Override
    public List<Request> getAllUsersRequestsPagination(String userId, int offset, int limit) {
        List<Request> requests = jdbcTemplate.query(
                SQLQueries.GET_USERS_CREATED_REQUESTS_PAGINATION, new Object[]{userId, limit, offset},
                new RowMapper<Request>() {
                    public Request mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                        return mapRequest(resultSet);
                    }
                });
        return requests;
    }

    @Override
    public void archiveOldRequests() {
        log.info("Archiving old requests for {}", dateFormat.format(new Date()));
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(SQLQueries.ARCHIVE_OLD_REQUESTS);
        log.info("Archiving ended on {}", dateFormat.format(new Date()));
    }

    @Override
    public List<Request> getAdminRequestsPagination(String id, int offset, int limit) {
        List<Request> requests = jdbcTemplate.query(
                SQLQueries.GET_ADMIN_REQUESTS_PAGINATION, new Object[]{limit, offset},
                new RowMapper<Request>() {
                    public Request mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                        return mapRequest(resultSet);
                    }
                });
        return requests;
    }

    @Override
    public List<Request> getKeeperRequestsPagination(String id, int offset, int limit) {
        List<Request> requests = jdbcTemplate.query(
                SQLQueries.GET_EXECUTORS_REQUESTS_PAGINATION, new Object[]{id, limit, offset},
                new RowMapper<Request>() {
                    public Request mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                        return mapRequest(resultSet);
                    }
                });
        return requests;
    }

    @Override
    public int getCountOfKeeperActiveRequests(String userId) {
        Integer count = jdbcTemplate.queryForObject(SQLQueries.GET_COUNT_EXECUTORS_REQUESTS, new Object[]{userId}, Integer.class);
        if (count == null) return 0;
        return count;
    }

    @Override
    public int getCountOfAdminActiveRequests() {
        Integer count = jdbcTemplate.queryForObject(SQLQueries.GET_COUNT_ADMIN_REQUESTS, Integer.class);
        if (count == null) return 0;
        return count;
    }
}
