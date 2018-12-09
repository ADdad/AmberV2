package amber_team.amber.dao.implementation;


import amber_team.amber.dao.interfaces.RoleDao;
import amber_team.amber.dao.interfaces.UserDao;
import amber_team.amber.model.dto.UserInfoDto;
import amber_team.amber.model.entities.User;
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
    private RoleDao roleDao;


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ResponseEntity save(User user) {
        if (!checkEmailAviability(user.getEmail())) {
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

    @Override
    public ResponseEntity getUserInfo(Principal principal) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        UserInfoDto info = jdbcTemplate.queryForObject(SQLQueries.USER_INFO_BY_USERNAME, new Object[]{principal.getName()}, new RowMapper<UserInfoDto>() {
            @Override
            public UserInfoDto mapRow(ResultSet resultSet, int i) throws SQLException {
                return getUserInfoDto(resultSet);
            }
        });
        info.setRoles(roleDao.getUserRoles(info.getId()));
        return ResponseEntity.ok(info);
    }

    @Override
    public UserInfoDto getUserByEmail(Principal principal) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        UserInfoDto info = jdbcTemplate.queryForObject(SQLQueries.USER_INFO_BY_USERNAME, new Object[]{principal.getName()}, new RowMapper<UserInfoDto>() {
            @Override
            public UserInfoDto mapRow(ResultSet resultSet, int i) throws SQLException {
                return getUserInfoDto(resultSet);
            }
        });
        info.setRoles(roleDao.getUserRoles(info.getId()));
        return info;
    }

    public List<UserInfoDto> getUsersPagination(int offset, int limit) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<UserInfoDto> users = jdbcTemplate.query(
                SQLQueries.GET_USERS_WITH_PAGINATION, new Object[]{limit, offset},
                new RowMapper<UserInfoDto>() {
                    public UserInfoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return getUserInfoDto(rs);
                    }
                });
        return users;
    }

    private UserInfoDto getUserInfoDto(ResultSet rs) throws SQLException {
        UserInfoDto info = new UserInfoDto();
        info.setId(rs.getString("id"));
        info.setEmail(rs.getString("email"));
        info.setFirstName(rs.getString("f_name"));
        info.setSecondName(rs.getString("s_name"));
        return info;
    }

    @Override
    public List<UserInfoDto> getAll() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<UserInfoDto> users = jdbcTemplate.query(
                SQLQueries.GET_ALL_USERS,
                new RowMapper<UserInfoDto>() {
                    public UserInfoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        UserInfoDto c = new UserInfoDto(rs.getString("id"), rs.getString("email"), rs.getString("f_name"), rs.getString("s_name"));
                        return c;
                    }
                });
        return users;
    }

    @Override
    public List<UserInfoDto> getAllActive() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<UserInfoDto> users = jdbcTemplate.query(
                SQLQueries.GET_ALL_ACTIVE_USERS,
                new RowMapper<UserInfoDto>() {
                    public UserInfoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        UserInfoDto c = new UserInfoDto(rs.getString("id"), rs.getString("email"), rs.getString("f_name"), rs.getString("s_name"));
                        return c;
                    }
                });
        return users;
    }


    @Override
    public User getById(String id) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        User info = jdbcTemplate.queryForObject(SQLQueries.GET_USER_BY_ID, new Object[]{id}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User info = new User();
                info.setId(resultSet.getString("id"));
                info.setEmail(resultSet.getString("email"));
                info.setFirstName(resultSet.getString("f_name"));
                info.setSecondName(resultSet.getString("s_name"));
                info.setPassword(resultSet.getString("password"));
                return info;
            }
        });
        return info;
    }

    private boolean checkEmailAviability(String email) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            jdbcTemplate.queryForObject(SQLQueries.EXISTING_THIS_EMAIL, new Object[]{email}, String.class);
        } catch (EmptyResultDataAccessException e) {
            return true;
        }
        return false;
    }


}
