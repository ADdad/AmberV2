package amber_team.amber.model.dto;


import java.util.List;

public class UpdateRolesDto {
    private String userId;
    private List<String> roles;



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }


}
