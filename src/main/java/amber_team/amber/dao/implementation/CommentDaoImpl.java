package amber_team.amber.dao.implementation;

import amber_team.amber.dao.interfaces.CommentDao;
import amber_team.amber.model.dto.CommentDto;
import amber_team.amber.model.entities.Comment;
import amber_team.amber.util.SQLQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository(value = "commentDao")
public class CommentDaoImpl implements CommentDao {


    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public Comment getById(String id) {
        return null;
    }

    @Override
    public List<Comment> getAll() {
        return null;
    }

    public void create(Comment comment) {
        jdbcTemplate = new JdbcTemplate(dataSource);

        String id = UUID.randomUUID().toString();

        Timestamp creationDate = Timestamp.valueOf(LocalDateTime.now());

        jdbcTemplate.update(SQLQueries.ADD_NEW_COMMENT, id, comment.getRequest().getId(), comment.getUser().getId(), comment.getText(), creationDate);

    }

    @Override
    public List<CommentDto> getForRequest(String requestId) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<CommentDto> comments = jdbcTemplate.query(
                SQLQueries.GET_COMMENTS_OF_REQUEST, new Object[]{requestId},
                new RowMapper<CommentDto>() {
                    public CommentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        CommentDto c = new CommentDto();
                        c.setId(rs.getString("id"));
                        c.setRequestId(requestId);
                        c.setUserId(rs.getString("user_id"));
                        c.setText(rs.getString("comment_text"));
                        c.setCreationDate(rs.getTimestamp("creation_date"));
                        return c;
                    }
                });
        return comments;
    }
}
