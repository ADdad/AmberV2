package amber_team.amber.dao.interfaces;



import amber_team.amber.model.entities.Request;
import amber_team.amber.model.dto.RequestStatusChangeDto;
import org.springframework.http.ResponseEntity;

public interface RequestDao {
    public ResponseEntity save(Request request);
    public ResponseEntity open(RequestStatusChangeDto request);
    public ResponseEntity cancel(RequestStatusChangeDto request);
    public ResponseEntity review(RequestStatusChangeDto request);
    public ResponseEntity reject(RequestStatusChangeDto request);
    public ResponseEntity progress(RequestStatusChangeDto request);
    public ResponseEntity hold(RequestStatusChangeDto request);
    public ResponseEntity deliver(RequestStatusChangeDto request);
    public ResponseEntity complete(RequestStatusChangeDto request);

    public ResponseEntity getRequestInfo(RequestStatusChangeDto principal);


}
