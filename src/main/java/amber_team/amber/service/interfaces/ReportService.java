package amber_team.amber.service.interfaces;

import amber_team.amber.model.dto.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

public interface ReportService {
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

    void createAvailableExcel(HttpServletResponse response, ReportAvailableEquipmentDto reportDto);

    void createNonAvailableExcel(HttpServletResponse response, ReportAvailableEquipmentDto reportDto);

    void createDeliveredExcel(HttpServletResponse response, ReportDeliveredEquipmentDto reportDto);

    void createEndingExcel(HttpServletResponse response, ReportEndingEquipmentDto reportDto);

    void createProcessedExcel(HttpServletResponse response, ReportOrdersDto reportDto);

    void createUnprocessedExcel(HttpServletResponse response, ReportOrdersDto reportDto);

    void createExecutedExcel(HttpServletResponse response, ReportOrdersWithUserDto reportDto);

    void createCreatedExcel(HttpServletResponse response, ReportOrdersWithUserDto reportDto);
}
