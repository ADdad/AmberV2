package amber_team.amber.controller;

import amber_team.amber.model.dto.*;
import amber_team.amber.service.interfaces.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController (ReportService reportService) {
        this.reportService = reportService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/reports/equipment/available")
    public ResponseEntity getAvailableEquipmentReport(@RequestBody ReportAvailableEquipmentDto reportDto) {
        return reportService.getAvailableEquipmentReport(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/reports/equipment/available/excel")
    public void downloadAvailableOutputExcel(HttpServletResponse response, @RequestBody ReportAvailableEquipmentDto reportDto) {
        reportService.createAvailableExcel(response, reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/reports/equipment/nonavailable")
    public ResponseEntity getNonAvailableEquipmentReport(@RequestBody ReportAvailableEquipmentDto reportDto) {
        return reportService.getNonAvailableEquipmentReport(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/reports/equipment/nonavailable/excel")
    public void downloadNonAvailableOutputExcel(HttpServletResponse response, @RequestBody ReportAvailableEquipmentDto reportDto) {
        reportService.createNonAvailableExcel(response, reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/reports/equipment/delivered")
    public ResponseEntity getDeliveredEquipmentReport(@RequestBody ReportDeliveredEquipmentDto reportDto) {
        return reportService.getDeliveredEquipmentReport(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/reports/equipment/delivered/excel")
    public void downloadDeliveredOutputExcel(HttpServletResponse response, @RequestBody ReportDeliveredEquipmentDto reportDto) {
        reportService.createDeliveredExcel(response, reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/reports/equipment/ending")
    public ResponseEntity getEndingEquipmentReport(@RequestBody ReportEndingEquipmentDto reportDto) {
        return reportService.getEndingEquipmentReport(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/reports/equipment/ending/excel")
    public void downloadEndingOutputExcel(HttpServletResponse response, @RequestBody ReportEndingEquipmentDto reportDto) {
        reportService.createEndingExcel(response, reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/reports/orders/processed")
    public ResponseEntity getProcessedOrdersReport(@RequestBody ReportOrdersDto reportDto) {
        return reportService.getProcessedOrdersReport(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/reports/orders/processed/excel")
    public void downloadProcessedOutputExcel(HttpServletResponse response, @RequestBody ReportOrdersDto reportDto) {
        reportService.createProcessedExcel(response, reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/reports/orders/unprocessed")
    public ResponseEntity getUnprocessedOrdersReport(@RequestBody ReportOrdersDto reportDto) {
        return reportService.getUnprocessedOrdersReport(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/reports/orders/unprocessed/excel")
    public void downloadUnprocessedOutputExcel(HttpServletResponse response, @RequestBody ReportOrdersDto reportDto) {
        reportService.createUnprocessedExcel(response, reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/reports/orders/executed")
    public ResponseEntity getExecutedOrdersReportBy(@RequestBody ReportOrdersWithUserDto reportDto) {
        return reportService.getExecutedOrdersReportBy(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/reports/orders/executed/excel")
    public void downloadExecutedOutputExcel(HttpServletResponse response, @RequestBody ReportOrdersWithUserDto reportDto) {
        reportService.createExecutedExcel(response, reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/reports/orders/created")
    public ResponseEntity getCreatedOrdersReportBy(@RequestBody ReportOrdersWithUserDto reportDto) {
        return reportService.getCreatedOrdersReportBy(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/reports/orders/created/excel")
    public void downloadCreatedOutputExcel(HttpServletResponse response, @RequestBody ReportOrdersWithUserDto reportDto) {
        reportService.createCreatedExcel(response, reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/warehouses-for-report")
    public ResponseEntity getWarehouses(@RequestBody PaginationDto helperDto) {
        return reportService.getWarehouses(helperDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/executors-for-report")
    public ResponseEntity getExecutors(@RequestBody PaginationDto helperDto) {
        return reportService.getExecutors(helperDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @PostMapping(value = "/creators-for-report")
    public ResponseEntity getCreators(@RequestBody PaginationDto helperDto) {
        return reportService.getCreators(helperDto);
    }


}
