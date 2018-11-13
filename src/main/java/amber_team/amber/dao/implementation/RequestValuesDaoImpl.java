package amber_team.amber.dao.implementation;

import amber_team.amber.dao.interfaces.RequestValuesDao;
import amber_team.amber.model.dto.AttributeSaveDto;
import amber_team.amber.util.SQLQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository(value = "requestValuesDao")
public class RequestValuesDaoImpl implements RequestValuesDao {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }



    @Override
    public void save(AttributeSaveDto attr, String request_id) {
        jdbcTemplate.update(SQLQueries.ADD_REQUEST_ATTRIBUTE, request_id, attr.getId(), attr.getValue());
    }
}
