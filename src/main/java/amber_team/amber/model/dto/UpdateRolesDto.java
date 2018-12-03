package amber_team.amber.model.dto;


import java.util.List;

public class UpdateRolesDto {
    private String userId;
    private List<Integer> roles;



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    public void setRole(Integer roles) {
        this.roles.add(roles);
    }

}
