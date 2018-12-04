package amber_team.amber.dao.interfaces;

import amber_team.amber.model.entities.Type;

import java.util.List;

public interface RequestTypeDao {
    Type getById(String id);

    Type getByRequestId(String id);

    Type getByName(String name);

    List<Type> getAll();
}
