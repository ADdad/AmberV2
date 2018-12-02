package amber_team.amber.dao.implementation;


import amber_team.amber.dao.interfaces.UserListDao;
import amber_team.amber.model.dto.UpdateRolesDto;
import amber_team.amber.model.dto.UpdateRolesListDto;
import amber_team.amber.model.dto.UserInfoDto;
import amber_team.amber.model.dto.UserListDto;
import amber_team.amber.model.entities.Role;
import amber_team.amber.util.SQLQueries;
import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.xml.ws.Response;
import java.security.Principal;
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
    public ResponseEntity update(UpdateRolesListDto userDtos) {
        System.out.println("______int update userlistdaoimpl________");
        jdbcTemplate = new JdbcTemplate(dataSource);
        System.out.println("______int update userlistdaoimpl________");
        String sql = SQLQueries.CHANGE_USERS_AND_THEIR_ROLES;
        String drop = SQLQueries.DROP_USER_ROLES;
        try {
//            jdbcTemplate.execute(drop);
            System.out.println("fdsafdsa_______"+userDtos.getU());
            System.out.println("fdsfdsa______________"+userDtos.getU().get(0));
            for (UpdateRolesDto u: userDtos.getU()) {
                System.out.println("______int"+u.toString());
                System.out.println("user: "+u);
//                System.out.println("roles: "+u);
                if(!u.getUserId().isEmpty() && !(u.getRoles().isEmpty())){
                    for (String r: u.getRoles()) {
                        System.out.println("______infdsfdsafdst"+r);

                        jdbcTemplate.update(sql,u.getUserId(),r);
                    }
                }

            }
        }catch (JDBCException e){
            System.out.println("______int update userlistdaoimpl________");
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
