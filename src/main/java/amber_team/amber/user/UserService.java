package amber_team.amber.user;

import org.springframework.http.ResponseEntity;


public interface UserService {

    ResponseEntity save(UserDto user);

}
