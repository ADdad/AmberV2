package amber_team.amber.service.interfaces;
import amber_team.amber.model.dto.AdminPageDto;
import amber_team.amber.model.dto.UserListDto;
import org.springframework.http.ResponseEntity;

import javax.xml.ws.Response;
import java.security.Principal;
import java.util.List;

public interface AdminService {
    ResponseEntity update(UserListDto userDtos);
    UserListDto getAdminInfo(Principal principal);
    UserListDto returnUsers();

    AdminPageDto getUsers(int pageNumber);
}
