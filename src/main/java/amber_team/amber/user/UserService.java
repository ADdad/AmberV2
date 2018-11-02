package amber_team.amber.user;

import org.springframework.http.ResponseEntity;

import java.security.Principal;


public interface UserService {

    ResponseEntity save(UserDto user);
    ResponseEntity getUserInfo(Principal principal);

}
