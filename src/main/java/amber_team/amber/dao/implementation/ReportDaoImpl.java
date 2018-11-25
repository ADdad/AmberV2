package amber_team.amber.dao.implementation;

import amber_team.amber.dao.interfaces.ReportDao;
import amber_team.amber.model.dto.*;
import amber_team.amber.util.SQLQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
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
                new Object[] {reportDto.getFromDate(), reportDto.getToDate(), limit, offset}, (resultSet, i) -> getReportOrdersResponseDto(resultSet));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity getUnprocessedOrdersReport(ReportOrdersDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber()*reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportOrdersResponseDto> response = jdbcTemplate.query(SQLQueries.GET_UNPROCESSED_ORDERS_WITH_PAGINATION,
                new Object[] {reportDto.getFromDate(), reportDto.getToDate(), limit, offset}, (resultSet, i) -> getReportOrdersResponseDto(resultSet));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity getExecutedOrdersReportBy(ReportOrdersWithUserDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber()*reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportOrdersResponseDto> response = jdbcTemplate.query(SQLQueries.GET_EXECUTED_ORDERS_BY_WITH_PAGINATION,
                new Object[] {reportDto.getFromDate(), reportDto.getToDate(), reportDto.getUserId(), limit, offset}, (resultSet, i) -> getReportOrdersResponseDto(resultSet));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity getCreatedOrdersReportBy(ReportOrdersWithUserDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber()*reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportOrdersResponseDto> response = jdbcTemplate.query(SQLQueries.GET_CREATED_ORDERS_BY_WITH_PAGINATION,
                new Object[] {reportDto.getFromDate(), reportDto.getToDate(), reportDto.getUserId(), limit, offset}, (resultSet, i) -> getReportOrdersResponseDto(resultSet));
        return ResponseEntity.ok(response);
    }

    private ReportOrdersResponseDto getReportOrdersResponseDto(ResultSet resultSet) throws SQLException {
        ReportOrdersResponseDto orderInfo = new ReportOrdersResponseDto();
        orderInfo.setCreationDate(resultSet.getDate("creation_date"));
        orderInfo.setCreatorEmail(resultSet.getString("creator_email"));
        orderInfo.setCreatorPib(resultSet.getString("creator_surname")+resultSet.getString("creator_name"));
        orderInfo.setExecutorEmail(resultSet.getString("executor_email"));
        orderInfo.setExecutorPib(resultSet.getString("executor_surname")+resultSet.getString("executor_name"));
        orderInfo.setModifiedDate(resultSet.getDate("modified_date"));
        orderInfo.setOrderDescription(resultSet.getString("description"));
        orderInfo.setOrderStatus(resultSet.getString("status"));
        orderInfo.setOrderType(resultSet.getString("order_type"));
        orderInfo.setWarehouseAddress(resultSet.getString("warehouse_address"));
        orderInfo.setWarehousePhone(resultSet.getString("warehouse_phone"));
        return null;
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
