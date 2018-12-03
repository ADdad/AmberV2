package amber_team.amber.dao.implementation;


import amber_team.amber.dao.interfaces.UserListDao;
import amber_team.amber.model.dto.UpdateRolesDto;
import amber_team.amber.model.dto.UpdateRolesListDto;
import amber_team.amber.model.dto.UserInfoDto;
import amber_team.amber.model.dto.UserListDto;
import amber_team.amber.model.entities.Role;
import amber_team.amber.model.entities.Type;
import amber_team.amber.util.SQLQueries;
import jdk.nashorn.internal.parser.JSONParser;
import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.xml.ws.Response;
import java.security.Principal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository(value = "UserListDao")
public class UserListDaoImpl implements UserListDao {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ResponseEntity update(UpdateRolesListDto userDtos1) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = SQLQueries.CHANGE_USERS_AND_THEIR_ROLES;
        String drop = SQLQueries.IF_EXISTS;
        UpdateRolesListDto userDtos = new UpdateRolesListDto();
        List<UpdateRolesDto> lst = new ArrayList<>();
        for(UpdateRolesDto u : userDtos1.getUsers()) {
            UpdateRolesDto d = new UpdateRolesDto();
            d.setRoles(u.getRoles());
            d.setUserId(u.getUserId());
            lst.add(d);
        }
        userDtos.setUsers(lst);
        try {
            for (UpdateRolesDto u: userDtos.getUsers()) {
                if(!u.getUserId().isEmpty() && !(u.getRoles().isEmpty())){
                    for (Integer r: u.getRoles()) {
                        Integer cnt = jdbcTemplate.queryForObject(
                                drop, Integer.class, u.getUserId(),r);

                        if(cnt != null && cnt > 0) continue;
                        jdbcTemplate.update(sql,u.getUserId(),r);
                    }
                }

            }
        }catch (JDBCException e){
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
        jdbcTemplate = new JdbcTemplate(dataSource);
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
