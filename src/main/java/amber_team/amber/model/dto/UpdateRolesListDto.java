package amber_team.amber.model.dto;

import java.util.List;

public class UpdateRolesListDto {


    public List<UpdateRolesDto> getUsers() {
        return users;
    }

    public void setUsers(List<UpdateRolesDto> users) {
        this.users = users;
    }

    private List<UpdateRolesDto> users;
}
