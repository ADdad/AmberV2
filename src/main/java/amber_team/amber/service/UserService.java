package amber_team.amber.service;

import amber_team.amber.model.User;
import amber_team.amber.model.UserDto;


public interface UserService {

    User save(UserDto user);

}
