package amber_team.amber.user;

import java.util.List;

public class UserInfoDto {
    private String id;
    private String email;
    private String firstName;
    private String secondName;
    private List<String> roles;

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
