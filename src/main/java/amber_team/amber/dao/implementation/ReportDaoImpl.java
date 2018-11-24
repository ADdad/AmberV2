package amber_team.amber.dao.implementation;

import amber_team.amber.dao.interfaces.ReportDao;
import amber_team.amber.model.dto.ReportAvailableEquipmentDto;
import amber_team.amber.model.dto.ReportDeliveredEquipmentDto;
import amber_team.amber.model.dto.ReportEndingEquipmentDto;
import amber_team.amber.model.dto.ReportEquipmentResponseDto;
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
        int offset = reportDto.getPageNumber()*reportDto.getResultsPerPage() - reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportEquipmentResponseDto> response = jdbcTemplate.query(SQLQueries.GET_AVAILABLE_EQUIPMENT_WITH_PAGINATION,
                    new Object[]{reportDto.getWarehouseId(), offset, limit}, (resultSet, rowNum) -> {
                        return getReportEquipmentResponseDto(resultSet);
                    });
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity getNonAvailableEquipmentReport(ReportAvailableEquipmentDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber()*reportDto.getResultsPerPage() - reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportEquipmentResponseDto> response = jdbcTemplate.query(SQLQueries.GET_NONAVAILABLE_EQUIPMENT_WITH_PAGINATION,
                new Object[]{reportDto.getWarehouseId(), offset, limit}, (resultSet, rowNum) -> {
                    return getReportEquipmentResponseDto(resultSet);
                });
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity getDeliveredEquipmentReport(ReportDeliveredEquipmentDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber()*reportDto.getResultsPerPage() - reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportEquipmentResponseDto> response = jdbcTemplate.query(SQLQueries.GET_DELIVERED_EQUIPMENT_WITH_PAGINATION,
                new Object[]{reportDto.getWarehouseId(),reportDto.getFromDate(),reportDto.getToDate(), offset, limit},
                (resultSet, rowNum) -> {
                    return getReportEquipmentResponseDto(resultSet);
                });
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity getEndingEquipmentReport(ReportEndingEquipmentDto reportDto) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        int offset = reportDto.getPageNumber()*reportDto.getResultsPerPage() - reportDto.getResultsPerPage();
        int limit = reportDto.getResultsPerPage();
        List<ReportEquipmentResponseDto> response = jdbcTemplate.query(SQLQueries.GET_ENDING_EQUIPMENT_WITH_PAGINATION,
                new Object[]{reportDto.getWarehouseId(),reportDto.getEquipmentThreshold(), offset, limit}, (resultSet, rowNum) -> {
                    return getReportEquipmentResponseDto(resultSet);
                });
        return ResponseEntity.ok(response);
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
