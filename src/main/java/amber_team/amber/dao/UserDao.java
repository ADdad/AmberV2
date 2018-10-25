package amber_team.amber.dao;


import amber_team.amber.model.User;
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
                    .body("{ErrorMessage : 'Your email is already registered'}");

        } else {

            String sql = "INSERT INTO users" +
                    " (id ,email, password, s_name, f_name) VALUES (?, ?, ?, ?, ?);" +
                    "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
            jdbcTemplate = new JdbcTemplate(dataSource);
            String id = UUID.randomUUID().toString();
            int role_id = 2; //Role_User
            jdbcTemplate.update(sql, new Object[]{id, user.getEmail(), user.getPassword(),
                    user.getSname(), user.getFname(), id, role_id});
            User result = new User();
            result.setId(id);
            result.setFname(user.getFname());
            result.setSname(user.getSname());
            result.setEmail(user.getEmail());
            return ResponseEntity.ok(result);
        }
    }

    private boolean checkEmailAviability(String email){
        String sql = "SELECT email FROM users WHERE email=?";
        jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            jdbcTemplate.queryForObject(sql, new Object[]{email}, String.class);
        } catch (EmptyResultDataAccessException e){
            return true;
        }
        return false;
    }


}
