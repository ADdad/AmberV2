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

    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public ResponseEntity getAvailableEquipmentReport(ReportAvailableEquipmentDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber()*reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportEquipmentResponseDto> response = jdbcTemplate.query(SQLQueries.GET_AVAILABLE_EQUIPMENT_WITH_PAGINATION,
                    new Object[]{reportDto.getWarehouseId(), limit, offset}, (resultSet, rowNum) -> {
                        return getReportEquipmentResponseDto(resultSet);
                    });
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity getNonAvailableEquipmentReport(ReportAvailableEquipmentDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber()*reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportEquipmentResponseDto> response = jdbcTemplate.query(SQLQueries.GET_NONAVAILABLE_EQUIPMENT_WITH_PAGINATION,
                new Object[]{reportDto.getWarehouseId(), limit, offset}, (resultSet, rowNum) -> {
                    return getReportEquipmentResponseDto(resultSet);
                });
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity getDeliveredEquipmentReport(ReportDeliveredEquipmentDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber()*reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportEquipmentResponseDto> response = jdbcTemplate.query(SQLQueries.GET_DELIVERED_EQUIPMENT_WITH_PAGINATION,
                new Object[]{reportDto.getWarehouseId(),reportDto.getFromDate(),reportDto.getToDate(),  limit, offset},
                (resultSet, rowNum) -> {
                    return getReportEquipmentResponseDto(resultSet);
                });
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity getEndingEquipmentReport(ReportEndingEquipmentDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber()*reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportEquipmentResponseDto> response = jdbcTemplate.query(SQLQueries.GET_ENDING_EQUIPMENT_WITH_PAGINATION,
                new Object[]{reportDto.getWarehouseId(),reportDto.getEquipmentThreshold(),  limit, offset}, (resultSet, rowNum) -> {
                    return getReportEquipmentResponseDto(resultSet);
                });
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity getProcessedOrdersReport(ReportOrdersDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber()*reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportOrdersResponseDto> response = jdbcTemplate.query(SQLQueries.GET_PROCESSED_ORDERS_WITH_PAGINATION,
                new Object[] {reportDto.getFromDate(), reportDto.getToDate(), limit, offset}, (resultSet, rowNum) -> {
                    return getReportOrdersResponseDto(resultSet);
            });
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity getUnprocessedOrdersReport(ReportOrdersDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber()*reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportOrdersResponseDto> response = jdbcTemplate.query(SQLQueries.GET_UNPROCESSED_ORDERS_WITH_PAGINATION,
                new Object[] {reportDto.getFromDate(), reportDto.getToDate(), limit, offset}, (resultSet, rowNum) ->  getReportOrdersResponseDto(resultSet));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity getExecutedOrdersReportBy(ReportOrdersWithUserDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber()*reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportOrdersResponseDto> response = jdbcTemplate.query(SQLQueries.GET_EXECUTED_ORDERS_BY_WITH_PAGINATION,
                new Object[] {reportDto.getFromDate(), reportDto.getToDate(), reportDto.getUserId(), limit, offset}, (resultSet, rowNum) ->  getReportOrdersResponseDto(resultSet));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity getCreatedOrdersReportBy(ReportOrdersWithUserDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber()*reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportOrdersResponseDto> response = jdbcTemplate.query(SQLQueries.GET_CREATED_ORDERS_BY_WITH_PAGINATION,
                new Object[] {reportDto.getFromDate(), reportDto.getToDate(), reportDto.getUserId(), limit, offset}, (resultSet, rowNum) ->  getReportOrdersResponseDto(resultSet));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity getWarehouses(PaginationDto helperDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = helperDto.getPageNumber()*helperDto.getResultsPerPage();
        int limit = helperDto.getResultsPerPage();
        List<ReportWarehouseDto> response = jdbcTemplate.query(SQLQueries.GET_WAREHOUSES_WITH_PAGINATION, new Object[] {limit, offset}, (resultSet, rowNum) -> {
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
        int offset = helperDto.getPageNumber()*helperDto.getResultsPerPage();
        int limit = helperDto.getResultsPerPage();
        List<ReportUserDto> response = jdbcTemplate.query(SQLQueries.GET_EXECUTORS_WITH_PAGINATION, new Object[] {limit, offset}, (resultSet,rowNum) -> {
            return getReportUserDto(resultSet);
        });
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity getCreators(PaginationDto helperDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = helperDto.getPageNumber()*helperDto.getResultsPerPage();
        int limit = helperDto.getResultsPerPage();
        List<ReportUserDto> response = jdbcTemplate.query(SQLQueries.GET_CREATORS_WITH_PAGINATION, new Object[] {limit, offset}, (resultSet,rowNum) -> {
            return getReportUserDto(resultSet);
        });
        return ResponseEntity.ok(response);
    }

    private ReportUserDto getReportUserDto(ResultSet resultSet) throws SQLException {
        ReportUserDto user = new ReportUserDto();
        user.setId(resultSet.getString("id"));
        user.setPib(resultSet.getString("name")+ " "+resultSet.getString("surname"));
        return user;
    }

    private ReportOrdersResponseDto getReportOrdersResponseDto(ResultSet resultSet) throws SQLException {
        ReportOrdersResponseDto orderInfo = new ReportOrdersResponseDto();
        orderInfo.setCreationDate(resultSet.getDate("creation_date"));
        orderInfo.setCreatorEmail(resultSet.getString("creator_email"));
        orderInfo.setCreatorPib(resultSet.getString("creator_surname")+" "+resultSet.getString("creator_name"));
        orderInfo.setExecutorEmail(resultSet.getString("executor_email"));
        orderInfo.setExecutorPib(resultSet.getString("executor_surname")+" "+resultSet.getString("executor_name"));
        orderInfo.setModifiedDate(resultSet.getDate("modified_date"));
        orderInfo.setOrderDescription(resultSet.getString("description"));
        orderInfo.setOrderStatus(resultSet.getString("status"));
        orderInfo.setOrderType(resultSet.getString("order_type"));
        orderInfo.setWarehouseAddress(resultSet.getString("warehouse_address"));
        orderInfo.setWarehousePhone(resultSet.getString("warehouse_phone"));
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
