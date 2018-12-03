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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

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
    @RequestMapping(value="/reports/equipment/available/excel", method = RequestMethod.POST)
    public ModelAndView downloadAvailableOutputExcel(HttpServletResponse response, @RequestBody ReportAvailableEquipmentDto reportDto){
        reportService.createAvailableExcel(response,reportDto);
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/equipment/nonavailable", method = RequestMethod.POST)
    public ResponseEntity getNonAvailableEquipmentReport(@RequestBody ReportAvailableEquipmentDto reportDto) {
        return reportService.getNonAvailableEquipmentReport(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/equipment/nonavailable/excel", method = RequestMethod.POST)
    public ModelAndView downloadNonAvailableOutputExcel(HttpServletResponse response, @RequestBody ReportAvailableEquipmentDto reportDto){
        reportService.createNonAvailableExcel(response,reportDto);
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/equipment/delivered", method = RequestMethod.POST)
    public ResponseEntity getDeliveredEquipmentReport(@RequestBody ReportDeliveredEquipmentDto reportDto) {
        return reportService.getDeliveredEquipmentReport(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/equipment/delivered/excel", method = RequestMethod.POST)
    public ModelAndView downloadDeliveredOutputExcel(HttpServletResponse response, @RequestBody ReportDeliveredEquipmentDto reportDto){
        reportService.createDeliveredExcel(response,reportDto);
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/equipment/ending", method = RequestMethod.POST)
    public ResponseEntity getEndingEquipmentReport(@RequestBody ReportEndingEquipmentDto reportDto) {
        return reportService.getEndingEquipmentReport(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/equipment/ending/excel", method = RequestMethod.POST)
    public ModelAndView downloadEndingOutputExcel(HttpServletResponse response, @RequestBody ReportEndingEquipmentDto reportDto){
        reportService.createEndingExcel(response,reportDto);
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/orders/processed", method = RequestMethod.POST)
    public ResponseEntity getProcessedOrdersReport(@RequestBody ReportOrdersDto reportDto) {
        return reportService.getProcessedOrdersReport(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/orders/processed/excel", method = RequestMethod.POST)
    public ModelAndView downloadProcessedOutputExcel(HttpServletResponse response, @RequestBody ReportOrdersDto reportDto){
        reportService.createProcessedExcel(response,reportDto);
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/orders/unprocessed", method = RequestMethod.POST)
    public ResponseEntity getUnprocessedOrdersReport(@RequestBody ReportOrdersDto reportDto) {
        return reportService.getUnprocessedOrdersReport(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/orders/unprocessed/excel", method = RequestMethod.POST)
    public ModelAndView downloadUnprocessedOutputExcel(HttpServletResponse response, @RequestBody ReportOrdersDto reportDto){
        reportService.createUnprocessedExcel(response,reportDto);
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/orders/executed", method = RequestMethod.POST)
    public ResponseEntity getExecutedOrdersReportBy(@RequestBody ReportOrdersWithUserDto reportDto) {
        return reportService.getExecutedOrdersReportBy(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/orders/executed/excel", method = RequestMethod.POST)
    public ModelAndView downloadExecutedOutputExcel(HttpServletResponse response, @RequestBody ReportOrdersWithUserDto reportDto){
        reportService.createExecutedExcel(response,reportDto);
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/orders/created", method = RequestMethod.POST)
    public ResponseEntity getCreatedOrdersReportBy(@RequestBody ReportOrdersWithUserDto reportDto) {
        return reportService.getCreatedOrdersReportBy(reportDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @RequestMapping(value="/reports/orders/created/excel", method = RequestMethod.POST)
    public ModelAndView downloadCreatedOutputExcel(HttpServletResponse response, @RequestBody ReportOrdersWithUserDto reportDto){
        reportService.createCreatedExcel(response,reportDto);
        return null;
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
