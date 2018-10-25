package amber_team.amber.user;



import org.springframework.http.ResponseEntity;

public interface IUserDao {
    public ResponseEntity save (User user);

}
