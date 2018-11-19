package amber_team.amber.dao.interfaces;

import amber_team.amber.model.dto.CommentDto;
import amber_team.amber.model.entities.Comment;

import java.util.List;

public interface CommentDao {

    Comment getById(String id);
    List<Comment> getAll();
    List<CommentDto> getForRequest(String requestId);
}
