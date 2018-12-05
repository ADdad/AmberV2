package amber_team.amber.service.implementation;

import amber_team.amber.dao.interfaces.ReportDao;
import amber_team.amber.model.dto.*;
import amber_team.amber.service.interfaces.ReportService;
import amber_team.amber.util.ErrorMessages;
import org.jxls.template.SimpleExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

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

    @Override
    public void createAvailableExcel(HttpServletResponse response, ReportAvailableEquipmentDto reportDto) {
        List<ReportEquipmentResponseDto> equipment = reportDao.getAllAvailableEquipment(reportDto);
        String fileName = "available";
        generateEquipmentExcel(equipment,response,fileName);
    }

    @Override
    public void createNonAvailableExcel(HttpServletResponse response, ReportAvailableEquipmentDto reportDto) {
        List<ReportEquipmentResponseDto> equipment = reportDao.getAllNonAvailableEquipment(reportDto);
        String fileName = "non-available";
        generateEquipmentExcel(equipment,response,fileName);
    }

    @Override
    public void createDeliveredExcel(HttpServletResponse response, ReportDeliveredEquipmentDto reportDto) {
        List<ReportEquipmentResponseDto> equipment = reportDao.getAllDeliveredEquipment(reportDto);
        String fileName = "delivered";
        generateEquipmentExcel(equipment,response,fileName);
    }

    @Override
    public void createEndingExcel(HttpServletResponse response, ReportEndingEquipmentDto reportDto) {
        List<ReportEquipmentResponseDto> equipment = reportDao.getAllEndingEquipment(reportDto);
        String fileName = "ending";
        generateEquipmentExcel(equipment,response,fileName);
    }

    @Override
    public void createProcessedExcel(HttpServletResponse response, ReportOrdersDto reportDto) {
        List<ReportOrdersResponseExcelDto> orders = reportDao.getAllProcessedOrders(reportDto);
        String fileName = "processed";
        generateOrdersExcel(orders,response,fileName);
    }

    @Override
    public void createUnprocessedExcel(HttpServletResponse response, ReportOrdersDto reportDto) {
        List<ReportOrdersResponseExcelDto> orders = reportDao.getAllUnprocessedOrders(reportDto);
        String fileName = "unprocessed";
        generateOrdersExcel(orders,response,fileName);
    }

    @Override
    public void createExecutedExcel(HttpServletResponse response, ReportOrdersWithUserDto reportDto) {
        List<ReportOrdersResponseExcelDto> orders = reportDao.getAllExecutedOrders(reportDto);
        String fileName = "executed-by-"+reportDto.getUserId();
        generateOrdersExcel(orders,response,fileName);
    }

    @Override
    public void createCreatedExcel(HttpServletResponse response, ReportOrdersWithUserDto reportDto) {
        List<ReportOrdersResponseExcelDto> orders = reportDao.getAllCreatedOrders(reportDto);
        String fileName = "created-by-"+reportDto.getUserId();
        generateOrdersExcel(orders,response,fileName);
    }

    private void generateEquipmentExcel(List<ReportEquipmentResponseDto> equipment, HttpServletResponse response, String fileName){
        List<String> headers = Arrays.asList("Model", "Quantity", "Producer", "Country");
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            response.addHeader("Content-disposition", "attachment; filename="+fileName+"-equipment-" + timestamp + ".xls");
            response.setContentType("application/vnd.ms-excel");
            new SimpleExporter().gridExport(headers, equipment, "equipmentModel, quantity, equipmentProducer," +
                    " equipmentCountry", response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {

        }
    }

    private void generateOrdersExcel(List<ReportOrdersResponseExcelDto> orders, HttpServletResponse response, String fileName) {
        List<String> headers = Arrays.asList("Order Type", "Status", "Creator", "Executor", "Creation date",
                "Last modified date", " Warehouse", "Title", "Description");
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            response.addHeader("Content-disposition", "attachment; filename="+fileName+"-orders-" + timestamp + ".xls");
            response.setContentType("application/vnd.ms-excel");
            new SimpleExporter().gridExport(headers, orders, "orderType, orderStatus, creator, executor," +
                    " creationDate, modifiedDate, warehouse, title, orderDescription", response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkReportAvailableEquipmentDto(ReportAvailableEquipmentDto reportDto) {
        return !(reportDto.getWarehouseId().isEmpty() || reportDto.getPageNumber() < 0 || reportDto.getResultsPerPage() < 0);
    }

}
