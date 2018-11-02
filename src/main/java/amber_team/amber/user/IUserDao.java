package amber_team.amber.user;



import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface IUserDao {
    public ResponseEntity save (User user);
    public ResponseEntity getUserInfo(Principal principal);
}
