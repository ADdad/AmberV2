package amber_team.amber.dao.interfaces;

import amber_team.amber.model.dto.AttributeDto;
import amber_team.amber.model.dto.AttributeInfoDto;

import java.util.List;

public interface AttributesDao {

    List<AttributeDto> getAttributesOfType(String type);

    List<AttributeInfoDto> getAttributesValuesOfRequest(String requestId);

    AttributeDto getById(String id);

    void addAttributeValueToRequest(List<AttributeInfoDto> attributeInfoDtos, String requestId);

    void removeRequestValues(String requestId);
}
