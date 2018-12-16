package amber_team.amber.service.interfaces;

import amber_team.amber.model.dto.UserDto;
import amber_team.amber.model.dto.UserListDto;
import org.springframework.http.ResponseEntity;

import java.security.Principal;


public interface UserService {

    ResponseEntity save(UserDto user);

    ResponseEntity getUserInfo(Principal principal);

    UserListDto searchUsers(String search);
}
