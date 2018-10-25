package amber_team.amber.dao.impl;


import amber_team.amber.dao.IUserDao;
import amber_team.amber.model.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    public User save(User user){
        String sql = "INSERT INTO users"+
                " (id ,email, password, s_name, f_name) VALUES (?, ?, ?, ?, ?);"+
                "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
        jdbcTemplate = new JdbcTemplate(dataSource);
        String id = UUID.randomUUID().toString();
        int role_id = 2; //Role_User
        jdbcTemplate.update(sql, new Object[] {id ,user.getEmail(), user.getPassword(),
        user.getSname(), user.getFname(), id, role_id});
        User result = new User();
        result.setId(id);
        result.setFname(user.getFname());

        result.setSname(user.getSname());
        result.setEmail(user.getEmail());
        return result;
    }


}
