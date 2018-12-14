package amber_team.amber.dao.implementation;

import amber_team.amber.dao.interfaces.ReportDao;
import amber_team.amber.model.dto.*;
import amber_team.amber.util.SQLQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.xml.ws.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ReportDaoImpl implements ReportDao {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ResponseEntity getAvailableEquipmentReport(ReportAvailableEquipmentDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber() * reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportEquipmentResponseDto> response =
                jdbcTemplate.query(SQLQueries.GET_AVAILABLE_EQUIPMENT_WITH_PAGINATION,
                new Object[]{reportDto.getWarehouseId(), limit, offset}, (resultSet, rowNum) -> {
                    return getReportEquipmentResponseDto(resultSet);
                });
        return ResponseEntity.ok(response);
    }

    @Override
    public List<ReportEquipmentResponseDto> getAllAvailableEquipment(ReportAvailableEquipmentDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(SQLQueries.GET_ALL_AVAILABLE_EQUIPMENT, new Object[]{reportDto.getWarehouseId()},
                (resultSet, i) -> {
            return getReportEquipmentResponseDto(resultSet);
        });
    }


    @Override
    public ResponseEntity getNonAvailableEquipmentReport(ReportAvailableEquipmentDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber() * reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportEquipmentResponseDto> response =
                jdbcTemplate.query(SQLQueries.GET_NONAVAILABLE_EQUIPMENT_WITH_PAGINATION,
                new Object[]{reportDto.getWarehouseId(), limit, offset}, (resultSet, rowNum) -> {
                    return getReportEquipmentResponseDto(resultSet);
                });
        return ResponseEntity.ok(response);
    }

    @Override
    public List<ReportEquipmentResponseDto> getAllNonAvailableEquipment(ReportAvailableEquipmentDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(SQLQueries.GET_ALL_NONAVAILABLE_EQIPMENT, new Object[]{reportDto.getWarehouseId()},
                (resultSet, i) -> {
            return getReportEquipmentResponseDto(resultSet);
        });
    }

    @Override
    public ResponseEntity getDeliveredEquipmentReport(ReportDeliveredEquipmentDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber() * reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportEquipmentResponseDto> response = jdbcTemplate.query(SQLQueries.GET_DELIVERED_EQUIPMENT_WITH_PAGINATION,
                new Object[]{reportDto.getWarehouseId(), reportDto.getFromDate(), reportDto.getToDate(), limit, offset},
                (resultSet, rowNum) -> {
                    return getReportEquipmentResponseDto(resultSet);
                });
        return ResponseEntity.ok(response);
    }

    @Override
    public List<ReportEquipmentResponseDto> getAllDeliveredEquipment(ReportDeliveredEquipmentDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(SQLQueries.GET_ALL_DELIVERED_EQUIPMENT, new Object[]{reportDto.getWarehouseId(),
                reportDto.getFromDate(), reportDto.getToDate()}, (resultSet, rowNum) -> {
            return getReportEquipmentResponseDto(resultSet);
        });
    }


    @Override
    public ResponseEntity getEndingEquipmentReport(ReportEndingEquipmentDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber() * reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportEquipmentResponseDto> response = jdbcTemplate.query(SQLQueries.GET_ENDING_EQUIPMENT_WITH_PAGINATION,
                new Object[]{reportDto.getWarehouseId(), reportDto.getEquipmentThreshold(), limit, offset},
                (resultSet, rowNum) -> {
                    return getReportEquipmentResponseDto(resultSet);
                });
        return ResponseEntity.ok(response);
    }

    @Override
    public List<ReportEquipmentResponseDto> getAllEndingEquipment(ReportEndingEquipmentDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(SQLQueries.GET_ALL_ENDING_EQUIPMENT, new Object[]{reportDto.getWarehouseId(),
                reportDto.getEquipmentThreshold()}, (resultSet, rowNum) -> {
            return getReportEquipmentResponseDto(resultSet);
        });
    }


    @Override
    public ResponseEntity getProcessedOrdersReport(ReportOrdersDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber() * reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportOrdersResponseDto> response = jdbcTemplate.query(SQLQueries.GET_PROCESSED_ORDERS_WITH_PAGINATION,
                new Object[]{reportDto.getFromDate(), reportDto.getToDate(), limit, offset}, (resultSet, rowNum) -> {
                    return getReportOrdersResponseDto(resultSet);
                });
        return ResponseEntity.ok(response);
    }

    @Override
    public List<ReportOrdersResponseExcelDto> getAllProcessedOrders(ReportOrdersDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(SQLQueries.GET_ALL_PROCESSED_ORDERS, new Object[]{reportDto.getFromDate(),
                reportDto.getToDate()}, (resultSet, rowNum) -> {
            return getReportOrdersResponseExcelDto(resultSet);
        });
    }


    @Override
    public ResponseEntity getUnprocessedOrdersReport(ReportOrdersDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber() * reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportOrdersResponseDto> response = jdbcTemplate.query(SQLQueries.GET_UNPROCESSED_ORDERS_WITH_PAGINATION,
                new Object[]{reportDto.getFromDate(), reportDto.getToDate(), limit, offset}, (resultSet, rowNum) ->
                        getReportOrdersResponseDto(resultSet));
        return ResponseEntity.ok(response);
    }

    @Override
    public List<ReportOrdersResponseExcelDto> getAllUnprocessedOrders(ReportOrdersDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(SQLQueries.GET_ALL_UNPROCESSED_ORDERS, new Object[]{reportDto.getFromDate(),
                reportDto.getToDate()}, (resultSet, rowNum) -> {
            return getReportOrdersResponseExcelDto(resultSet);
        });
    }


    @Override
    public ResponseEntity getExecutedOrdersReportBy(ReportOrdersWithUserDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber() * reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportOrdersResponseDto> response = jdbcTemplate.query(SQLQueries.GET_EXECUTED_ORDERS_BY_WITH_PAGINATION,
                new Object[]{reportDto.getFromDate(), reportDto.getToDate(), reportDto.getUserId(), limit, offset},
                (resultSet, rowNum) -> getReportOrdersResponseDto(resultSet));
        return ResponseEntity.ok(response);
    }

    @Override
    public List<ReportOrdersResponseExcelDto> getAllExecutedOrders(ReportOrdersWithUserDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(SQLQueries.GET_ALL_EXECUTED_ORDERS, new Object[]{reportDto.getFromDate(),
                reportDto.getToDate(), reportDto.getUserId()}, (resultSet, rowNum) -> {
            return getReportOrdersResponseExcelDto(resultSet);
        });
    }

    @Override
    public ResponseEntity getCreatedOrdersReportBy(ReportOrdersWithUserDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber() * reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportOrdersResponseDto> response = jdbcTemplate.query(SQLQueries.GET_CREATED_ORDERS_BY_WITH_PAGINATION,
                new Object[]{reportDto.getFromDate(), reportDto.getToDate(), reportDto.getUserId(), limit, offset},
                (resultSet, rowNum) -> getReportOrdersResponseDto(resultSet));
        return ResponseEntity.ok(response);
    }

    @Override
    public List<ReportOrdersResponseExcelDto> getAllCreatedOrders(ReportOrdersWithUserDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(SQLQueries.GET_ALL_CREATED_ORDERS, new Object[]{reportDto.getFromDate(),
                reportDto.getToDate(), reportDto.getUserId()}, (resultSet, rowNum) -> {
            return getReportOrdersResponseExcelDto(resultSet);
        });
    }

    @Override
    public ResponseEntity getWarehouses(PaginationDto helperDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = helperDto.getPageNumber() * helperDto.getResultsPerPage();
        int limit = helperDto.getResultsPerPage();
        List<ReportWarehouseDto> response = jdbcTemplate.query(SQLQueries.GET_WAREHOUSES_WITH_PAGINATION,
                new Object[]{limit, offset}, (resultSet, rowNum) -> {
            ReportWarehouseDto warehouse = new ReportWarehouseDto();
            warehouse.setId(resultSet.getString("id"));
            warehouse.setAddress(resultSet.getString("address"));
            return warehouse;
        });
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity getExecutors(PaginationDto helperDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = helperDto.getPageNumber() * helperDto.getResultsPerPage();
        int limit = helperDto.getResultsPerPage();
        List<ReportUserDto> response = jdbcTemplate.query(SQLQueries.GET_EXECUTORS_WITH_PAGINATION,
                new Object[]{limit, offset}, (resultSet, rowNum) -> {
            return getReportUserDto(resultSet);
        });
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity getCreators(PaginationDto helperDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = helperDto.getPageNumber() * helperDto.getResultsPerPage();
        int limit = helperDto.getResultsPerPage();
        List<ReportUserDto> response = jdbcTemplate.query(SQLQueries.GET_CREATORS_WITH_PAGINATION,
                new Object[]{limit, offset}, (resultSet, rowNum) -> {
            return getReportUserDto(resultSet);
        });
        return ResponseEntity.ok(response);
    }


    private ReportUserDto getReportUserDto(ResultSet resultSet) throws SQLException {
        ReportUserDto user = new ReportUserDto();
        user.setId(resultSet.getString("id"));
        user.setPib(resultSet.getString("name") + " " + resultSet.getString("surname"));
        return user;
    }

    private ReportOrdersResponseDto getReportOrdersResponseDto(ResultSet resultSet) throws SQLException {
        ReportOrdersResponseDto orderInfo = new ReportOrdersResponseDto();
        orderInfo.setCreationDate(resultSet.getDate("creation_date"));
        orderInfo.setCreatorEmail(resultSet.getString("creator_email"));
        orderInfo.setCreatorPib(resultSet.getString("creator_surname") + " " +
                resultSet.getString("creator_name"));
        orderInfo.setExecutorEmail(resultSet.getString("executor_email"));
        if (resultSet.wasNull()) {
            orderInfo.setExecutorEmail("");
        }
        orderInfo.setExecutorPib(resultSet.getString("executor_surname") + " " +
                resultSet.getString("executor_name"));
        if (resultSet.wasNull()) {
            orderInfo.setExecutorPib("");
        }
        orderInfo.setModifiedDate(resultSet.getDate("modified_date"));
        orderInfo.setOrderDescription(resultSet.getString("description"));
        orderInfo.setOrderStatus(resultSet.getString("status"));
        orderInfo.setOrderType(resultSet.getString("order_type"));
        orderInfo.setWarehouseAddress(resultSet.getString("warehouse_address"));
        orderInfo.setWarehousePhone(resultSet.getString("warehouse_phone"));
        orderInfo.setTitle(resultSet.getString("title"));
        return orderInfo;
    }

    private ReportOrdersResponseExcelDto getReportOrdersResponseExcelDto(ResultSet resultSet) throws SQLException {
        ReportOrdersResponseExcelDto orderInfo = new ReportOrdersResponseExcelDto();
        orderInfo.setCreationDate(resultSet.getDate("creation_date"));
        orderInfo.setCreator(resultSet.getString("creator_surname") + " " +
                resultSet.getString("creator_name")
                + " " + resultSet.getString("creator_email"));
        orderInfo.setExecutor(resultSet.getString("executor_surname") + " " +
                resultSet.getString("executor_name")
                + " " + resultSet.getString("executor_email"));
        orderInfo.setModifiedDate(resultSet.getDate("modified_date"));
        orderInfo.setOrderDescription(resultSet.getString("description"));
        orderInfo.setOrderStatus(resultSet.getString("status"));
        orderInfo.setOrderType(resultSet.getString("order_type"));
        orderInfo.setWarehouse(resultSet.getString("warehouse_address") + " " +
                resultSet.getString("warehouse_phone"));
        orderInfo.setTitle(resultSet.getString("title"));
        return orderInfo;
    }

    private ReportEquipmentResponseDto getReportEquipmentResponseDto(ResultSet resultSet) throws SQLException {
        ReportEquipmentResponseDto equipment = new ReportEquipmentResponseDto();
        equipment.setQuantity(resultSet.getInt("quantity"));
        equipment.setEquipmentCountry(resultSet.getString("country"));
        equipment.setEquipmentModel(resultSet.getString("model"));
        equipment.setEquipmentProducer(resultSet.getString("producer"));
        return equipment;
    }
}
