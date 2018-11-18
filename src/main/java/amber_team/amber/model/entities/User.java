package amber_team.amber.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


public class User {


    private String id;
    private String email;
    @JsonIgnore
    private String password;
    private String fName;
    private String sName;
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

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getSName() {
        return sName;
    }

    public void setSName(String sName) {
        this.sName = sName;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean addComment(Comment comment) { return comments.add(comment); }
}
