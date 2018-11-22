package amber_team.amber.dao.interfaces;


import amber_team.amber.model.dto.AttributeDto;
import amber_team.amber.model.entities.Request;
import amber_team.amber.model.dto.RequestStatusChangeDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RequestDao {
    public Request create(Request request);

    public Request update(Request request);

    public Request getById(Request requestId);

    public void archiveOldRequests();

}
