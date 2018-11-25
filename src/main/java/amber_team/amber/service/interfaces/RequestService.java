package amber_team.amber.service.interfaces;

import amber_team.amber.model.dto.*;
import amber_team.amber.model.entities.Request;


public interface RequestService {

    RequestInfoDto getRequestInfo(String id);

    Request save(RequestSaveDto request);

    void archiveOldRequests();

    CreateOrderDto creationData(String type);

    EquipmentSearchDto searchEquipment(String value);

    EquipmentListDto unavailableEquipmentByRequestId(String requestId);

    UserListDto getWarehouseExecutors(String warehouseId);

    Request changeStatus(MyRequestStatusChangeDto request);

    Request editRequest(RequestSaveDto request);
}
