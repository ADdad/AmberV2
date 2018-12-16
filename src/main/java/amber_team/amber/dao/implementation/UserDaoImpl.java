package amber_team.amber.dao.implementation;


import amber_team.amber.dao.interfaces.RoleDao;
import amber_team.amber.dao.interfaces.UserDao;
import amber_team.amber.model.dto.UpdateRolesDto;
import amber_team.amber.model.dto.UpdateRolesListDto;
import amber_team.amber.model.dto.UserInfoDto;
import amber_team.amber.model.dto.UserListDto;
import amber_team.amber.model.entities.User;
import amber_team.amber.util.Constants;
import amber_team.amber.util.SQLQueries;
import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.security.Principal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static amber_team.amber.util.Constants.USERS_SEARCH_LIMIT;

@Repository(value = "userDao")
public class UserDaoImpl implements UserDao {
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final RoleDao roleDao;

    @Autowired
    public UserDaoImpl(DataSource dataSource, RoleDao roleDao) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.roleDao = roleDao;
    }


    public ResponseEntity save(User user) {
        String id = UUID.randomUUID().toString();
        jdbcTemplate.update(SQLQueries.ADD_NEW_USER_AND_HIS_ROLE, id, user.getEmail(), user.getPassword(),
                user.getSecondName(), user.getFirstName(), Constants.ENABLED_USER, id, Constants.ROLE_USER);
        User result = new User();
        result.setId(id);
        result.setFirstName(user.getFirstName());
        result.setSecondName(user.getSecondName());
        result.setEmail(user.getEmail());
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity getUserInfo(Principal principal) {

        UserInfoDto info = jdbcTemplate.queryForObject(SQLQueries.USER_INFO_BY_USERNAME,
                new Object[]{principal.getName()}, new RowMapper<UserInfoDto>() {
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

        UserInfoDto info = jdbcTemplate.queryForObject(SQLQueries.USER_INFO_BY_USERNAME,
                new Object[]{principal.getName()}, new RowMapper<UserInfoDto>() {
                    @Override
                    public UserInfoDto mapRow(ResultSet resultSet, int i) throws SQLException {
                        return getUserInfoDto(resultSet);
                    }
                });
        info.setRoles(roleDao.getUserRoles(info.getId()));
        return info;
    }

    public List<UserInfoDto> getUsersPagination(int offset, int limit) {

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

        List<UserInfoDto> users = jdbcTemplate.query(
                SQLQueries.GET_ALL_USERS,
                new RowMapper<UserInfoDto>() {
                    public UserInfoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        UserInfoDto c = new UserInfoDto(rs.getString("id"),
                                rs.getString("email"), rs.getString("f_name"),
                                rs.getString("s_name"));
                        return c;
                    }
                });
        return users;
    }

    @Override
    public List<UserInfoDto> getAllActive() {

        List<UserInfoDto> users = jdbcTemplate.query(
                SQLQueries.GET_ALL_ACTIVE_USERS,
                new RowMapper<UserInfoDto>() {
                    public UserInfoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        UserInfoDto c = new UserInfoDto(rs.getString("id"),
                                rs.getString("email"), rs.getString("f_name"),
                                rs.getString("s_name"));
                        return c;
                    }
                });
        return users;
    }


    @Override
    public User getById(String id) {

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

    @Override
    public UserInfoDto getByIdWithRoles(String id) {

        UserInfoDto info = jdbcTemplate.queryForObject(SQLQueries.GET_USER_BY_ID, new Object[]{id},
                new RowMapper<UserInfoDto>() {
                    @Override
                    public UserInfoDto mapRow(ResultSet resultSet, int i) throws SQLException {
                        return getUserInfoDto(resultSet);
                    }
                });
        info.setRoles(roleDao.getUserRoles(info.getId()));
        return info;
    }

    public boolean checkEmailAvailability(String email) {

        try {
            jdbcTemplate.queryForObject(SQLQueries.EXISTING_THIS_EMAIL, new Object[]{email}, String.class);
        } catch (EmptyResultDataAccessException e) {
            return true;
        }
        return false;
    }

    @Override
    public List<UserInfoDto> searchUsers(String search) {
        String formatValue = "%" + search + "%";

        List<UserInfoDto> users = jdbcTemplate.query(
                SQLQueries.FIND_USERS_BY_VALUE, new Object[]{formatValue, formatValue, formatValue, USERS_SEARCH_LIMIT},
                (rs, rowNum) -> getUserInfoDto(rs));
        return users;
    }


    private void enableUser(String userId, boolean enable) {
        int enabled = 0;
        if (enable) enabled = 1;
        jdbcTemplate.update(SQLQueries.UPDATE_ENABLED_USER, enabled, userId);
    }

    @Override
    public ResponseEntity update(UpdateRolesListDto userDtos) {
        NamedParameterJdbcTemplate jdbcTemplateN = new NamedParameterJdbcTemplate(dataSource);
        String sql = SQLQueries.CHANGE_USERS_AND_THEIR_ROLES;
        try {
            for (UpdateRolesDto u : userDtos.getUsers()) {
                if (!u.getUserId().isEmpty()) {
                    if (u.getRoles().size() < 1) {
                        enableUser(u.getUserId(), false);
                    } else {
                        MapSqlParameterSource parameters = new MapSqlParameterSource();
                        parameters.addValue("roles", u.getRoles());
                        parameters.addValue("user_id", u.getUserId());


                        jdbcTemplateN.update(SQLQueries.CLEAR_USERS_ROLES,
                                parameters);
                        for (Integer r : u.getRoles()) {
                            jdbcTemplate.update(sql, u.getUserId(), r);
                        }
                    }
                }

            }
        } catch (JDBCException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Some error while invoking db");
        }


        return ResponseEntity.ok().body("Done");
    }


    @Override
    public UserListDto returnUsers() {
        String sql = SQLQueries.USERS_INFO;

        List<UserInfoDto> customers = new ArrayList<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        for (Map row : rows) {
            UserInfoDto customer = new UserInfoDto();
            customer.setId((String) (row.get("id")));
            customer.setEmail((String) (row.get("email")));
            customer.setFirstName((String) (row.get("f_name")));
            customer.setSecondName((String) (row.get("s_name")));

            customers.add(customer);
        }
        UserListDto userListDto = new UserListDto();
        userListDto.setList(customers);
        return userListDto;
    }

}
