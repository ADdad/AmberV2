package amber_team.amber.dao.implementation;

import amber_team.amber.dao.interfaces.AttributesDao;
import amber_team.amber.model.dto.AttributeDto;
import amber_team.amber.model.dto.AttributeInfoDto;
import amber_team.amber.util.SQLQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Repository(value = "attributesDao")
public class AttributesDaoImpl implements AttributesDao {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public List<AttributeDto> getAttributesOfType(String type) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        List<AttributeDto> attributeDtoList = jdbcTemplate.query(
                SQLQueries.REQUEST_ATTRIBUTES_BY_TYPE,
                new Object[]{type},
                new RowMapper<AttributeDto>() {
                    public AttributeDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        AttributeDto c = new AttributeDto();
                        c.setId(rs.getString(1));
                        c.setName(rs.getString(2));
                        c.setType(rs.getString(3));
                        c.setOrder(rs.getInt(4));
                        c.setMultiple(rs.getBoolean(5));
                        c.setMandatory(rs.getBoolean(6));
                        c.setImmutable(rs.getBoolean(7));
                        return c;
                    }
                });
        for (AttributeDto attr :
                attributeDtoList) {
            getAttributeValues(attr, jdbcTemplate);
        }
        return attributeDtoList;
    }


    public void addAttributeValueToRequest(List<AttributeInfoDto> attributeInfoDtos, String requestId) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.batchUpdate(SQLQueries.ADD_REQUEST_ATTRIBUTE, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                AttributeInfoDto attributeInfoDto = attributeInfoDtos.get(i);
                ps.setString(1, requestId);
                ps.setString(2, attributeInfoDto.getId());
                try {
                    if (attributeInfoDto.getType().equals("date")) {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(attributeInfoDto.getValue());
                        Timestamp timestamp = new java.sql.Timestamp(date.getTime());
                        ps.setString(3, null);
                        ps.setTimestamp(4, timestamp);
                        ps.setNull(5, java.sql.Types.NUMERIC);
                    } else if (attributeInfoDto.getType().equals("numeric")) {
                        double value = Double.parseDouble(attributeInfoDto.getValue());
                        ps.setString(3, null);
                        ps.setNull(4, Types.TIMESTAMP);
                        ps.setDouble(5, value);
                    } else {
                        ps.setString(3, attributeInfoDto.getValue());
                        ps.setNull(4, Types.TIMESTAMP);
                        ps.setNull(5, java.sql.Types.NUMERIC);
                    }
                } catch (Exception e) { //this generic but you can control another types of exception
                    ps.setString(3, attributeInfoDto.getValue());
                    ps.setNull(4, Types.TIMESTAMP);
                    ps.setNull(5, java.sql.Types.NUMERIC);
                }
            }

            @Override
            public int getBatchSize() {
                return attributeInfoDtos.size();
            }
        });
    }

    @Override
    public List<AttributeInfoDto> getAttributesValuesOfRequest(String requestId) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        //TODO add formating to date and time and numeric

        List<AttributeInfoDto> attributeInfoDtos = jdbcTemplate.query(
                SQLQueries.REQUEST_ATTRIBUTES_VALUES_BY_ID,
                new Object[]{requestId},
                new RowMapper<AttributeInfoDto>() {
                    public AttributeInfoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        AttributeInfoDto c = new AttributeInfoDto();
                        c.setId(rs.getString("id"));
                        c.setName(rs.getString("name"));
                        c.setType(rs.getString("type"));
                        c.setOrder(rs.getInt("attr_order"));
                        String string_value = rs.getString("string_value");
                        if (string_value != null && !string_value.isEmpty()) {
                            c.setValue(string_value);
                            return c;
                        }
                        Timestamp date_value = rs.getTimestamp("date_value");
                        if (date_value != null) {
                            c.setValue(date_value.toString());
                            return c;
                        }
                        Double decimal_value = rs.getDouble("decimal_value");
                        c.setValue(decimal_value.toString());
                        return c;


                    }
                });

        return attributeInfoDtos;
    }

    @Override
    public AttributeDto getById(String id) {
        return null;
    }


    @Override
    public void removeRequestValues(String requestId) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(SQLQueries.DELETE_REQUEST_VALUES, requestId);
    }

    private void getAttributeValues(AttributeDto attr, JdbcTemplate jdbcTemplate) {
        List<String> values = jdbcTemplate.query(SQLQueries.RESERVED_VALUES_FOR_ATTRIBUTE_ID, new Object[]{attr.getId()}, new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getString(1);
            }
        });
        attr.setValues(values);
    }
}
