package amber_team.amber.dao.implementation;

import amber_team.amber.dao.interfaces.RoleDao;
import amber_team.amber.model.entities.Role;
import amber_team.amber.util.SQLQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository(value = "roleDao")
public class RoleDaoImpl implements RoleDao {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Role> getUserRoles(String id) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Role> roles = jdbcTemplate.query(
                SQLQueries.USER_ROLES_BY_ID, new Object[]{id},
                new RowMapper<Role>() {
                    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Role c = new Role(rs.getString(1), rs.getString(2));
                        return c;
                    }
                });
        return roles;
    }

    @Override
    public List<Role> getAll() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Role> roles = jdbcTemplate.query(
                SQLQueries.GET_ALL_ROLES,
                new RowMapper<Role>() {
                    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Role c = new Role(rs.getString(1), rs.getString(2));
                        return c;
                    }
                });
        return roles;
    }
}
