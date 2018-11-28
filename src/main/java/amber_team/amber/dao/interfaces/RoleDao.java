package amber_team.amber.dao.interfaces;

import amber_team.amber.model.entities.Role;

import java.util.List;

public interface RoleDao {

    List<Role> getUserRoles(String id);
    List<Role> getAll();
}
