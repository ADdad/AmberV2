package amber_team.amber.dao.interfaces;

import amber_team.amber.model.dto.AttributeDto;
import amber_team.amber.model.dto.AttributeSaveDto;
import amber_team.amber.model.entities.Attribute;

import java.util.List;

public interface RequestValuesDao {

    void save(AttributeSaveDto attr, String request_id);

}
