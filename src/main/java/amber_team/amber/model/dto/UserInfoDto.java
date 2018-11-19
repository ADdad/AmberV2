package amber_team.amber.model.dto;

import amber_team.amber.model.entities.User;

import java.util.List;

public class UserInfoDto {
    private String id;
    private String email;
    private String firstName;
    private String secondName;
    private List<String> roles;

    public UserInfoDto(){}

    public UserInfoDto(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.secondName = user.getSecondName();
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
