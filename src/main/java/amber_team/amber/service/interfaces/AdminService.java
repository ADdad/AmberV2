package amber_team.amber.service.interfaces;
import amber_team.amber.model.dto.UserListDto;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface AdminService {
    ResponseEntity update(UserListDto userDtos);
    ResponseEntity getAdminInfo(Principal principal);
    ResponseEntity returnUsers();
}
