package amber_team.amber.service;

import amber_team.amber.model.UserDto;
import org.springframework.http.ResponseEntity;

import java.security.Principal;


public interface IUserService {

    ResponseEntity save(UserDto user);
    ResponseEntity getUserInfo(Principal principal);

}
