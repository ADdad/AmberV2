package amber_team.amber.service.interfaces;

import amber_team.amber.model.dto.RequestSaveDto;
import amber_team.amber.model.dto.RequestStatusChangeDto;
import org.springframework.http.ResponseEntity;


public interface RequestService {

    ResponseEntity getRequestInfo(RequestStatusChangeDto request);
    ResponseEntity save(RequestSaveDto request);
    ResponseEntity open(RequestStatusChangeDto request);
    ResponseEntity cancel(RequestStatusChangeDto request);
    ResponseEntity reject(RequestStatusChangeDto request);
    ResponseEntity review(RequestStatusChangeDto request);
    ResponseEntity progress(RequestStatusChangeDto request);
    ResponseEntity hold(RequestStatusChangeDto request);
    ResponseEntity deliver(RequestStatusChangeDto request);
    ResponseEntity complete(RequestStatusChangeDto request);
}
