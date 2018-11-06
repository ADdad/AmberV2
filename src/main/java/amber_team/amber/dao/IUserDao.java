package amber_team.amber.dao;



import amber_team.amber.model.User;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface IUserDao {
    public ResponseEntity save (User user);
    public ResponseEntity getUserInfo(Principal principal);
}
