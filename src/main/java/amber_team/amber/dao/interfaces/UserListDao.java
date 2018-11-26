package amber_team.amber.dao.interfaces;


import amber_team.amber.model.dto.UserListDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.security.Principal;

public interface UserListDao {
    UserListDto update(UserListDto userDtos);
    ResponseEntity getAdminInfo(Principal principal);
    UserListDto returnUsers();
}
