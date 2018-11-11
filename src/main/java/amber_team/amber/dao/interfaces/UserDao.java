package amber_team.amber.dao.interfaces;



import amber_team.amber.model.entities.User;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface UserDao {
    public ResponseEntity save (User user);
    public ResponseEntity getUserInfo(Principal principal);
}
