package amber_team.amber.dao.interfaces;

import amber_team.amber.model.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

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

    List<ReportEquipmentResponseDto> getAllAvailableEquipment(ReportAvailableEquipmentDto reportDto);

    List<ReportEquipmentResponseDto> getAllNonAvailableEquipment(ReportAvailableEquipmentDto reportDto);

    List<ReportEquipmentResponseDto> getAllDeliveredEquipment(ReportDeliveredEquipmentDto reportDto);

    List<ReportEquipmentResponseDto> getAllEndingEquipment(ReportEndingEquipmentDto reportDto);

    List<ReportOrdersResponseExcelDto> getAllProcessedOrders(ReportOrdersDto reportDto);

    List<ReportOrdersResponseExcelDto> getAllUnprocessedOrders(ReportOrdersDto reportDto);

    List<ReportOrdersResponseExcelDto> getAllExecutedOrders(ReportOrdersWithUserDto reportDto);

    List<ReportOrdersResponseExcelDto> getAllCreatedOrders(ReportOrdersWithUserDto reportDto);
}
