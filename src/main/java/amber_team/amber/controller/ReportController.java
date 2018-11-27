package amber_team.amber.controller;

import amber_team.amber.model.dto.ReportAvailableEquipmentDto;
import amber_team.amber.model.dto.ReportDeliveredEquipmentDto;
import amber_team.amber.model.dto.ReportEndingEquipmentDto;
import amber_team.amber.service.interfaces.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PreAuthorize("hasAnyRole(ROLE_ADMIN,ROLE_KEEPER)")
    @RequestMapping(value="/reports/equipment/available", method = RequestMethod.GET)
    public ResponseEntity getAvailableEquipmentReport(ReportAvailableEquipmentDto reportDto) {
        return reportService.getAvailableEquipmentReport(reportDto);
    }

    @PreAuthorize("hasAnyRole(ROLE_ADMIN,ROLE_KEEPER)")
    @RequestMapping(value="/reports/equipment/nonavailable", method = RequestMethod.GET)
    public ResponseEntity getNonAvailableEquipmentReport(ReportAvailableEquipmentDto reportDto) {
        return reportService.getNonAvailableEquipmentReport(reportDto);
    }

    @PreAuthorize("hasAnyRole(ROLE_ADMIN,ROLE_KEEPER)")
    @RequestMapping(value="/reports/equipment/delivered", method = RequestMethod.GET)
    public ResponseEntity getDeliveredEquipmentReport(ReportDeliveredEquipmentDto reportDto) {
        return reportService.getDeliveredEquipmentReport(reportDto);
    }

    @PreAuthorize("hasAnyRole(ROLE_ADMIN,ROLE_KEEPER)")
    @RequestMapping(value="/reports/equipment/ending", method = RequestMethod.GET)
    public ResponseEntity getEndingEquipmentReport(ReportEndingEquipmentDto reportDto) {
        return reportService.getEndingEquipmentReport(reportDto);
    }

}