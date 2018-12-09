package amber_team.amber.model.dto;

import amber_team.amber.model.entities.Role;
import amber_team.amber.model.entities.User;

import java.util.List;

public class UserInfoDto {
    private String id;
    private String email;
    private String firstName;
    private String secondName;
    private List<Role> roles;

    public UserInfoDto() {
    }

    public UserInfoDto(String id, String email, String firstName, String secondName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public UserInfoDto(User user) {
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

    public List<Role> getRoles() {
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

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
