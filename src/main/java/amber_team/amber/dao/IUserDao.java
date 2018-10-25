package amber_team.amber.dao;


import amber_team.amber.model.User;
import org.springframework.http.ResponseEntity;

public interface IUserDao {
    public ResponseEntity save (User user);

}
