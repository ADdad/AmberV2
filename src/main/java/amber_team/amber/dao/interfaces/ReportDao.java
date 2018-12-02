package amber_team.amber.dao.interfaces;

import amber_team.amber.model.dto.*;
import org.springframework.http.ResponseEntity;

public interface ReportDao {
    ResponseEntity getAvailableEquipmentReport(ReportAvailableEquipmentDto reportDto);

    ResponseEntity getNonAvailableEquipmentReport(ReportAvailableEquipmentDto reportDto);

    ResponseEntity getDeliveredEquipmentReport(ReportDeliveredEquipmentDto reportDto);

    ResponseEntity getEndingEquipmentReport(ReportEndingEquipmentDto reportDto);

    ResponseEntity getProcessedOrdersReport(ReportOrdersDto reportDto);

    ResponseEntity getUnprocessedOrdersReport(ReportOrdersDto reportDto);

    ResponseEntity getExecutedOrdersReportBy(ReportOrdersWithUserDto reportDto);

    ResponseEntity getCreatedOrdersReportBy(ReportOrdersWithUserDto reportDto);

    ResponseEntity getWarehouses(PaginationDto helperDto);

    ResponseEntity getExecutors(PaginationDto helperDto);

    ResponseEntity getCreators(PaginationDto helperDto);
}
