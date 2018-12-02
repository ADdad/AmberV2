package amber_team.amber.dao.interfaces;


import amber_team.amber.model.dto.UserListDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.xml.ws.Response;
import java.security.Principal;

public interface UserListDao {
    ResponseEntity update(UserListDto userDtos);
    ResponseEntity getAdminInfo(Principal principal);
    UserListDto returnUsers();
}
