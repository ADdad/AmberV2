package amber_team.amber.service.interfaces;

import amber_team.amber.model.dto.*;
import org.springframework.http.ResponseEntity;

public interface ReportService {
    ResponseEntity getAvailableEquipmentReport(ReportAvailableEquipmentDto reportDto);

    ResponseEntity getNonAvailableEquipmentReport(ReportAvailableEquipmentDto reportDto);

    ResponseEntity getDeliveredEquipmentReport(ReportDeliveredEquipmentDto reportDto);

    ResponseEntity getEndingEquipmentReport(ReportEndingEquipmentDto reportDto);

    ResponseEntity getProcessedOrdersReport(ReportOrdersDto reportDto);

    ResponseEntity getUnprocessedOrdersReport(ReportOrdersDto reportDto);

    ResponseEntity getExecutedOrdersReportBy(ReportOrdersWithUserDto reportDto);

    ResponseEntity getCreatedOrdersReportBy(ReportOrdersWithUserDto reportDto);
}
