package amber_team.amber.dao.interfaces;



import amber_team.amber.model.dto.AttributeDto;
import amber_team.amber.model.entities.Request;
import amber_team.amber.model.dto.RequestStatusChangeDto;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import java.util.List;

public interface RequestDao {
    public Request save(Request request);
    public ResponseEntity open(RequestStatusChangeDto request);
    public ResponseEntity cancel(RequestStatusChangeDto request);
    public ResponseEntity review(RequestStatusChangeDto request);
    public ResponseEntity reject(RequestStatusChangeDto request);
    public ResponseEntity progress(RequestStatusChangeDto request);
    public ResponseEntity hold(RequestStatusChangeDto request);
    public ResponseEntity deliver(RequestStatusChangeDto request);
    public ResponseEntity complete(RequestStatusChangeDto request);
    public List<AttributeDto> attributes(String type);

    public ResponseEntity getRequestInfo(Request principal);

    public void archiveOldRequests();

}
