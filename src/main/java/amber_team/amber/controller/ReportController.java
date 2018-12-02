package amber_team.amber.controller;

import amber_team.amber.model.dto.*;
import amber_team.amber.service.interfaces.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/equipment/available", method = RequestMethod.POST)
    public ResponseEntity getAvailableEquipmentReport(@RequestBody ReportAvailableEquipmentDto reportDto) {
        return reportService.getAvailableEquipmentReport(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/equipment/nonavailable", method = RequestMethod.POST)
    public ResponseEntity getNonAvailableEquipmentReport(@RequestBody ReportAvailableEquipmentDto reportDto) {
        return reportService.getNonAvailableEquipmentReport(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/equipment/delivered", method = RequestMethod.POST)
    public ResponseEntity getDeliveredEquipmentReport(@RequestBody ReportDeliveredEquipmentDto reportDto) {
        return reportService.getDeliveredEquipmentReport(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/equipment/ending", method = RequestMethod.POST)
    public ResponseEntity getEndingEquipmentReport(@RequestBody ReportEndingEquipmentDto reportDto) {
        return reportService.getEndingEquipmentReport(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/orders/processed", method = RequestMethod.POST)
    public ResponseEntity getProcessedOrdersReport(@RequestBody ReportOrdersDto reportDto) {
        return reportService.getProcessedOrdersReport(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/orders/unprocessed", method = RequestMethod.POST)
    public ResponseEntity getUnprocessedOrdersReport(@RequestBody ReportOrdersDto reportDto) {
        return reportService.getUnprocessedOrdersReport(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/orders/executed", method = RequestMethod.POST)
    public ResponseEntity getExecutedOrdersReportBy(@RequestBody ReportOrdersWithUserDto reportDto) {
        return reportService.getExecutedOrdersReportBy(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/orders/created", method = RequestMethod.POST)
    public ResponseEntity getCreatedOrdersReportBy(@RequestBody ReportOrdersWithUserDto reportDto) {
        return reportService.getCreatedOrdersReportBy(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/warehouses-for-report", method = RequestMethod.POST)
    public ResponseEntity getWarehouses(@RequestBody PaginationDto helperDto) {
        return reportService.getWarehouses(helperDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/executors-for-report", method = RequestMethod.POST)
    public ResponseEntity getExecutors(@RequestBody PaginationDto helperDto) {
        return reportService.getExecutors(helperDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/creators-for-report", method = RequestMethod.POST)
    public ResponseEntity getCreators(@RequestBody PaginationDto helperDto) {
        return reportService.getCreators(helperDto);
    }

}
