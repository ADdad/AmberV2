package amber_team.amber.model.entities;

import amber_team.amber.model.dto.CommentDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Comment {

    private String id;
    private Request request;
    private User user;
    private String text;
    private Timestamp creationDate;

    public Comment() {
    }

    public Comment(CommentDto commentDto){
        this.id = commentDto.getId();
        this.text = commentDto.getText();
        this.creationDate = commentDto.getCreationDate();
    }

    public Comment(CommentDto commentDto, User user){
        this.id = commentDto.getId();
        this.text = commentDto.getText();
        this.creationDate = commentDto.getCreationDate();
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
}
