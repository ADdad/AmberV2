package amber_team.amber.dao.implementation;

import amber_team.amber.dao.interfaces.EmailDao;
import amber_team.amber.util.SQLQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository(value = "emailDao")
public class EmailDaoImpl implements EmailDao {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public String getRegistrationTemplate() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.queryForObject(SQLQueries.GET_REGISTRATION_EMAIL_TEMPLATE, String.class);
    }

    @Override
    public String getRequestStatusChangedTemplate() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.queryForObject(SQLQueries.GET_REQUEST_STATUS_CHANGED_EMAIL_TEMPLATE,String.class);
    }

    @Override
    public String getUserRolesChangedTemplate() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.queryForObject(SQLQueries.GET_USER_ROLES_CHANGED_EMAIL_TEMPLATE, String.class);
    }

    @Override
    public String getRequestCreatedTemplate() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.queryForObject(SQLQueries.GET_REQUEST_CREATED_EMAIL_TEMPLATE, String.class);
    }
}
