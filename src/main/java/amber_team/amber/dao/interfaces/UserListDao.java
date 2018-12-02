package amber_team.amber.dao.interfaces;


import amber_team.amber.model.dto.UpdateRolesDto;
import amber_team.amber.model.dto.UpdateRolesListDto;
import amber_team.amber.model.dto.UserListDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.xml.ws.Response;
import java.security.Principal;
import java.util.List;

public interface UserListDao {
    ResponseEntity update(UpdateRolesListDto userDtos);
    ResponseEntity getAdminInfo(Principal principal);
    UserListDto returnUsers();
}
