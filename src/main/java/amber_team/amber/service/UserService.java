package amber_team.amber.service;

import amber_team.amber.model.User;
import amber_team.amber.model.UserDto;
import org.springframework.http.ResponseEntity;


public interface UserService {

    ResponseEntity save(UserDto user);

}
