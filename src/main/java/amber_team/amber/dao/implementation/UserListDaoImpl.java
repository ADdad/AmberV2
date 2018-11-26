package amber_team.amber.dao.implementation;


import amber_team.amber.dao.interfaces.UserListDao;
import amber_team.amber.model.dto.UserInfoDto;
import amber_team.amber.model.dto.UserListDto;
import amber_team.amber.model.entities.User;
import amber_team.amber.util.SQLQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import javax.sql.DataSource;
import java.security.Principal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Repository(value = "UserListDao")
public class UserListDaoImpl implements UserListDao {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ResponseEntity update(UserListDto userDtos) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = SQLQueries.CHANGE_USERS_AND_THEIR_ROLES;

        for (UserInfoDto u: userDtos.getList()
             ) {

        }

        return null;
    }

    @Override
    public ResponseEntity getAdminInfo(Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity returnUsers() {
        jdbcTemplate = new JdbcTemplate(dataSource);
            String sql = SQLQueries.USERS_INFO;

            List<UserInfoDto> customers =new ArrayList<>();

            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        for (Map row : rows) {
                UserInfoDto customer = new UserInfoDto();
                customer.setId((String)(row.get("id")));
                customer.setEmail((String)(row.get("email")));
                customer.setFirstName((String)(row.get("f_name")));
                customer.setSecondName((String)(row.get("s_name")));
                System.out.println("Name from row: "+(String)row.get("name"));
                customer.setRoles(new ArrayList<String>(Arrays.asList((String) (row.get("name")))));
                System.out.println("Added role to customer: "+ customer.getRoles().toString());
                customers.add(customer);
        }
        return ResponseEntity.ok(customers);
    }

}
