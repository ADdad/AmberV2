package amber_team.amber.service;

import amber_team.amber.model.RequestSaveDto;
import amber_team.amber.model.RequestStatusChangeDto;
import org.springframework.http.ResponseEntity;


public interface IRequestService {

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
