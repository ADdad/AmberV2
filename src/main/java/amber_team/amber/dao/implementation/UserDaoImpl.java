package amber_team.amber.dao.implementation;


import amber_team.amber.dao.interfaces.UserDao;
import amber_team.amber.model.entities.User;
import amber_team.amber.model.dto.UserInfoDto;
import amber_team.amber.service.implementation.EmailServiceImpl;
import amber_team.amber.util.EmailTexts;
import amber_team.amber.util.ErrorMessages;
import amber_team.amber.util.SQLQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.security.Principal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository(value = "userDao")
public class UserDaoImpl implements UserDao {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EmailServiceImpl emailService;


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
                    user.getSName(), user.getFName(), enabled, id, role_id});
            User result = new User();
            result.setId(id);
            result.setFName(user.getFName());
            result.setSName(user.getSName());
            result.setEmail(user.getEmail());
            emailService.sendSimpleMessage(user.getEmail(),"Registration", EmailTexts.REGISTERED);
            return ResponseEntity.ok(result);
        }
    }

    @Override
    public ResponseEntity getUserInfo(Principal principal) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        UserInfoDto info = jdbcTemplate.queryForObject(SQLQueries.USER_INFO_BY_USERNAME, new Object[]{principal.getName()}, new RowMapper<UserInfoDto>() {
            @Override
            public UserInfoDto mapRow(ResultSet resultSet, int i) throws SQLException {
                UserInfoDto info = new UserInfoDto();
                info.setId(resultSet.getString("id"));
                info.setEmail(resultSet.getString("email"));
                info.setFirstName(resultSet.getString("f_name"));
                info.setSecondName(resultSet.getString("s_name"));
                return info;
            }
        });
        List<String> roles = jdbcTemplate.queryForList(SQLQueries.USER_ROLES_BY_ID, new Object[] {info.getId()}, String.class);
        info.setRoles(roles);
        return ResponseEntity.ok(info);
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
