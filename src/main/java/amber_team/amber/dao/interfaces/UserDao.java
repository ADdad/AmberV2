package amber_team.amber.dao.interfaces;


import amber_team.amber.model.dto.UpdateRolesListDto;
import amber_team.amber.model.dto.UserInfoDto;
import amber_team.amber.model.dto.UserListDto;
import amber_team.amber.model.entities.User;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface UserDao {
    ResponseEntity save(User user);

    ResponseEntity getUserInfo(Principal principal);

    List<UserInfoDto> getAllActive();

    User getById(String id);

    UserInfoDto getByIdWithRoles(String id);

    UserInfoDto getUserByEmail(Principal principal);

    List<UserInfoDto> getUsersPagination(int offset, int limit);

    List<UserInfoDto> getAll();

    boolean checkEmailAvailability(String email);

    List<UserInfoDto> searchUsers(String search);

    ResponseEntity update(UpdateRolesListDto userDtos);

    UserListDto returnUsers();
}
