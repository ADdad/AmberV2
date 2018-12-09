package amber_team.amber.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


public class User {


    private String id;
    private String email;
    @JsonIgnore
    private String password;
    private String firstName;
    private String secondName;
    // todo add comments to service
    private List<Comment> comments;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean addComment(Comment comment) {
        return comments.add(comment);
    }
}
