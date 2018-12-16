package amber_team.amber.service.interfaces;

import amber_team.amber.model.dto.*;
import amber_team.amber.model.entities.Request;

import java.security.Principal;


public interface RequestService {

    RequestInfoDto getRequestInfo(String id);

    Request save(RequestSaveDto request);

    RequestListDtoPagination getCreatedRequests(Principal userData, int page, boolean archive);

    void archiveOldRequests();

    CreateOrderDto creationData(String type);

    EquipmentSearchDto searchEquipment(String value);

    UserListDto getWarehouseExecutors(String warehouseId);

    Request changeStatus(RequestStatusChangeDto request);

    void changeStatus(ListRequestChangeStatusDto request);

    Request editRequest(RequestSaveDto request);

    RequestListDtoPagination getExecutingRequests(Principal principal, int page, boolean archive);

}
