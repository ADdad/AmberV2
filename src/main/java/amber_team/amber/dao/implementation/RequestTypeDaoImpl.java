package amber_team.amber.dao.implementation;

import amber_team.amber.dao.interfaces.RequestTypeDao;
import amber_team.amber.model.entities.Equipment;
import amber_team.amber.model.entities.Type;
import amber_team.amber.util.SQLQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository(value = "requestTypeDao")
public class RequestTypeDaoImpl implements RequestTypeDao {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }



    @Override
    public Type getById(String id) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        Type type = jdbcTemplate.queryForObject(SQLQueries.TYPE_BY_ID, new Object[]{id}, new RowMapper<Type>() {
            @Override
            public Type mapRow(ResultSet resultSet, int i) throws SQLException {
                Type info = new Type();
                info.setId(resultSet.getString("id"));
                info.setName(resultSet.getString("name"));
                info.setCreationDate(resultSet.getTimestamp("creation_date"));
                return info;
            }
        });

        return type;
    }

    @Override
    public Type getByRequestId(String id) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        Type type = jdbcTemplate.queryForObject(SQLQueries.TYPE_BY_REQUEST_ID, new Object[]{id}, new RowMapper<Type>() {
            @Override
            public Type mapRow(ResultSet resultSet, int i) throws SQLException {
                Type info = new Type();
                info.setId(resultSet.getString("id"));
                info.setName(resultSet.getString("name"));
                info.setCreationDate(resultSet.getTimestamp("creation_date"));
                return info;
            }
        });
        return type;
    }

    @Override
    public Type getByName(String name) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        Type type = jdbcTemplate.queryForObject(SQLQueries.TYPE_BY_NAME, new Object[]{name}, new RowMapper<Type>() {
            @Override
            public Type mapRow(ResultSet resultSet, int i) throws SQLException {
                Type info = new Type();
                info.setId(resultSet.getString("id"));
                info.setName(resultSet.getString("name"));
                info.setCreationDate(resultSet.getTimestamp("creation_date"));
                return info;
            }
        });

        return type;
    }

    @Override
    public List<Type> getAll() {
        return null;
    }
}
