package amber_team.amber.service.interfaces;

import amber_team.amber.model.dto.ReportAvailableEquipmentDto;
import amber_team.amber.model.dto.ReportDeliveredEquipmentDto;
import amber_team.amber.model.dto.ReportEndingEquipmentDto;
import org.springframework.http.ResponseEntity;

public interface ReportService {
    ResponseEntity getAvailableEquipmentReport(ReportAvailableEquipmentDto reportDto);

    ResponseEntity getNonAvailableEquipmentReport(ReportAvailableEquipmentDto reportDto);

    ResponseEntity getDeliveredEquipmentReport(ReportDeliveredEquipmentDto reportDto);

    ResponseEntity getEndingEquipmentReport(ReportEndingEquipmentDto reportDto);
}
