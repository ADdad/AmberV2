package amber_team.amber.user;


import amber_team.amber.util.ErrorMessages;
import amber_team.amber.util.SQLQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.UUID;

@Repository(value = "userDao")
public class UserDao implements IUserDao {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public ResponseEntity save(User user){
        if(!checkEmailAviability(user.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(ErrorMessages.ALREADY_REGISTERED_EMAIL);

        } else {
            jdbcTemplate = new JdbcTemplate(dataSource);
            String id = UUID.randomUUID().toString();
            int role_id = 2; //Role_User
            int enabled = 1;
            jdbcTemplate.update(SQLQueries.ADD_NEW_USER_AND_HIS_ROLE, new Object[]{id, user.getEmail(), user.getPassword(),
                    user.getSecondName(), user.getFirstName(), enabled, id, role_id});
            User result = new User();
            result.setId(id);
            result.setFirstName(user.getFirstName());
            result.setSecondName(user.getSecondName());
            result.setEmail(user.getEmail());
            return ResponseEntity.ok(result);
        }
    }

    private boolean checkEmailAviability(String email){
        jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            jdbcTemplate.queryForObject(SQLQueries.EXISTING_THIS_EMAIL, new Object[]{email}, String.class);
        } catch (EmptyResultDataAccessException e){
            return true;
        }
        return false;
    }


}
