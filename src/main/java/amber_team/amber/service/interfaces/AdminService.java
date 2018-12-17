package amber_team.amber.service.interfaces;

import amber_team.amber.model.dto.*;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface AdminService {
    ResponseEntity update(UpdateRolesListDto userDtos);

    UserListDto getUsers(int pageNumber);

    AdminPageUsersDataDto getUsersData();

    ResponseEntity enableUser(String userId, boolean value);
}
