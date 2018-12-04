package amber_team.amber.model.dto;

import amber_team.amber.model.entities.Role;

import java.util.List;

public class AdminPageUsersDataDto {
    private int usersCount;
    private List<Role> systemRoles;

    public int getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }

    public List<Role> getSystemRoles() {
        return systemRoles;
    }

    public void setSystemRoles(List<Role> systemRoles) {
        this.systemRoles = systemRoles;
    }
}
