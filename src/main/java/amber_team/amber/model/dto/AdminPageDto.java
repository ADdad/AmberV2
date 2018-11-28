package amber_team.amber.model.dto;

import amber_team.amber.model.entities.Role;

import java.util.List;

public class AdminPageDto {

    private List<UserInfoDto> users;

    public List<UserInfoDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfoDto> users) {
        this.users = users;
    }

    public List<Role> getSystemRoles() {
        return systemRoles;
    }

    public void setSystemRoles(List<Role> systemRoles) {
        this.systemRoles = systemRoles;
    }

    private List<Role> systemRoles;



}
