package amber_team.amber.service.interfaces;

import amber_team.amber.model.dto.*;
import amber_team.amber.model.entities.Request;
import org.springframework.http.ResponseEntity;


public interface RequestService {

    ResponseEntity getRequestInfo(String id);
    ResponseEntity save(RequestSaveDto request);
    void archiveOldRequests();
    ResponseEntity creationData(String type);
    ResponseEntity searchEquipment(String value);
    ResponseEntity unavailableEquipmentByRequestId(String requestId);
    ResponseEntity getWarehouseExecutors(String warehouseId);
    ResponseEntity<Request> changeStatus(MyRequestStatusChangeDto request);
}
