package amber_team.amber.service.implementation;

import amber_team.amber.dao.interfaces.ReportDao;
import amber_team.amber.model.dto.*;
import amber_team.amber.service.interfaces.ReportService;
import amber_team.amber.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportDao reportDao;

    @Override
    public ResponseEntity getAvailableEquipmentReport(ReportAvailableEquipmentDto reportDto) {
        if(checkReportAvailableEquipmentDto(reportDto)) {
            return reportDao.getAvailableEquipmentReport(reportDto);
        } else {
            return ResponseEntity.badRequest().body(ErrorMessages.BLANK_INPUTS);
        }
    }

    @Override
    public ResponseEntity getNonAvailableEquipmentReport(ReportAvailableEquipmentDto reportDto) {
        if(checkReportAvailableEquipmentDto(reportDto)) {
            return reportDao.getNonAvailableEquipmentReport(reportDto);
        } else {
            return ResponseEntity.badRequest().body(ErrorMessages.BLANK_INPUTS);
        }
    }

    @Override
    public ResponseEntity getDeliveredEquipmentReport(ReportDeliveredEquipmentDto reportDto) {
        if(checkReportAvailableEquipmentDto(reportDto)){
            return reportDao.getDeliveredEquipmentReport(reportDto);
        } else {
            return ResponseEntity.badRequest().body(ErrorMessages.BLANK_INPUTS);
        }
    }

    @Override
    public ResponseEntity getEndingEquipmentReport(ReportEndingEquipmentDto reportDto) {
        if(checkReportAvailableEquipmentDto(reportDto)) {
            return reportDao.getEndingEquipmentReport(reportDto);
        } else {
            return ResponseEntity.badRequest().body(ErrorMessages.BLANK_INPUTS);
        }
    }

    @Override
    public ResponseEntity getProcessedOrdersReport(ReportOrdersDto reportDto) {
        return reportDao.getProcessedOrdersReport(reportDto);
    }

    @Override
    public ResponseEntity getUnprocessedOrdersReport(ReportOrdersDto reportDto) {
        return reportDao.getUnprocessedOrdersReport(reportDto);
    }

    @Override
    public ResponseEntity getExecutedOrdersReportBy(ReportOrdersWithUserDto reportDto) {
        return reportDao.getExecutedOrdersReportBy(reportDto);
    }

    @Override
    public ResponseEntity getCreatedOrdersReportBy(ReportOrdersWithUserDto reportDto) {
        return reportDao.getCreatedOrdersReportBy(reportDto);
    }

    @Override
    public ResponseEntity getWarehouses(PaginationDto helperDto) {
        return reportDao.getWarehouses(helperDto);
    }

    @Override
    public ResponseEntity getExecutors(PaginationDto helperDto) {
        return reportDao.getExecutors(helperDto);
    }

    @Override
    public ResponseEntity getCreators(PaginationDto helperDto) {
        return reportDao.getCreators(helperDto);
    }

    private boolean checkReportAvailableEquipmentDto(ReportAvailableEquipmentDto reportDto) {
        return !(reportDto.getWarehouseId().isEmpty() || reportDto.getPageNumber() < 0 || reportDto.getResultsPerPage() < 0);
    }

}
