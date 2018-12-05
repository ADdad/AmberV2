package amber_team.amber.dao.implementation;


import amber_team.amber.dao.interfaces.UserListDao;
import amber_team.amber.model.dto.UpdateRolesDto;
import amber_team.amber.model.dto.UpdateRolesListDto;
import amber_team.amber.model.dto.UserInfoDto;
import amber_team.amber.model.dto.UserListDto;
import amber_team.amber.util.SQLQueries;
import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository(value = "UserListDao")
public class UserListDaoImpl implements UserListDao {
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserListDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

//    @Override
//    public ResponseEntity update(UpdateRolesListDto userDtos) {
//
//        String sql = SQLQueries.CHANGE_USERS_AND_THEIR_ROLES;
//        String drop = SQLQueries.IF_EXISTS;
//
//        try {
//            for (UpdateRolesDto u : userDtos.getUsers()) {
//                if (!u.getUserId().isEmpty() && !(u.getRoles().isEmpty())) {
//                    jdbcTemplate.update("DELETE FROM user_roles WHERE user_id = ? AND role_id NOT IN ?",
//                            u.getUserId(), u.getRoles());
//                    for (Integer r : u.getRoles()) {
//                        Integer cnt = jdbcTemplate.queryForObject(
//                                drop, Integer.class, u.getUserId(), r);
//
//                        if (cnt != null && cnt > 0) continue;
//                        jdbcTemplate.update(sql, u.getUserId(), r);
//                    }
//                }
//
//            }
//        } catch (JDBCException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("Some error while invoking db");
//        }
//
//
//        return ResponseEntity.ok().body("Done");
//    }

    @Override
    public ResponseEntity update(UpdateRolesListDto userDtos) {
        NamedParameterJdbcTemplate jdbcTemplateN = new NamedParameterJdbcTemplate(dataSource);
        String sql = SQLQueries.CHANGE_USERS_AND_THEIR_ROLES;
        try {
            for (UpdateRolesDto u : userDtos.getUsers()) {
                if (!u.getUserId().isEmpty()) {

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
        } catch (JDBCException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Some error while invoking db");
        }


        return ResponseEntity.ok().body("Done");
    }


    @Override
    public ResponseEntity getAdminInfo(Principal principal) {
        return null;
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
