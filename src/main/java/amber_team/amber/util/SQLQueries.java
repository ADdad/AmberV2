package amber_team.amber.util;

public class SQLQueries {
    public static final String ADD_NEW_USER_AND_HIS_ROLE = "BEGIN; INSERT INTO users" +
            " (id ,email, password, s_name, f_name, enabled) VALUES (?, ?, ?, ?, ?, ?);" +
            "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?); COMMIT;";
    public static final String EXISTING_THIS_EMAIL = "SELECT email FROM users WHERE email=?";
    public static final String USER_BY_USERNAME_QUERY = "SELECT users.email as username, users.password as password, enabled " +
            "FROM users WHERE users.email=?";
    public static final String AUTHORITIES_BY_USERNAME = "SELECT users.email as username, roles.name as role " +
            "FROM users " +
            "INNER JOIN user_roles ON users.id = user_roles.user_id " +
            "INNER JOIN roles ON user_roles.role_id = roles.id " +
            "WHERE users.email=?";
    public static final String USER_INFO_BY_USERNAME = "SELECT id, email, f_name, s_name " +
            "FROM users WHERE users.email=?";
    public static final String USER_ROLES_BY_ID = "SELECT roles.name " +
            "FROM roles " +
            "INNER JOIN user_roles ON roles.id = user_roles.role_id " +
            "WHERE user_roles.user_id=?";
    public static final String TYPE_BY_ID = "SELECT * " +
            "FROM request_types " +
            "WHERE id=? ORDER BY creation_date DESC LIMIT 1";
    public static final String TYPE_BY_NAME = "SELECT * " +
            "FROM request_types " +
            "WHERE name=? ORDER BY creation_date DESC LIMIT 1";
    public static final String ADD_NEW_REQUEST = "INSERT INTO requests " +
            "(id, creator_id, req_type_id, status, creation_date, modified_date, description, archive, warehouse_id, title) VALUES (?,?,?,?,?,?,?,?,?,?)";
    public static final String CHANGE_REQUEST_STATUS_AND_CREATOR_ID = "UPDATE requests " +
            "SET creator_id = ?, status = ?, modified_date = ?" +
            "WHERE id = ?";
    public static final String CHANGE_REQUEST_STATUS = "UPDATE requests" +
            " SET status = ?, modified_date = ?" +
            " WHERE id = ?";
    public static final String CHANGE_REQUEST_STATUS_AND_EXECUTOR_ID = "UPDATE requests " +
            "SET executor_id = ?, status = ?, modified_date = ?" +
            "WHERE id = ?";
    public static final String REQUEST_INFO_BY_ID = "SELECT warehouse_id, creator_id, executor_id, req_type_id," +
            " title, status, creation_date, modified_date, description, archive " +
            "FROM requests " +
            "WHERE requests.id = ?";
    //    public static final String REQUEST_ATTRIBUTES_BY_ID = "SELECT attributes.name AS name, attribute_types.name AS type, string_value, date_value, decimal_value " +
//            "FROM request_values INNER JOIN (SELECT * FROM attributes INNER JOIN attribute_types ON attributes.attr_type_id=attribute_types.id) AS A1 ON request_values.attr_id=A1.id " +
//            "WHERE request_id = ?";
    public static final String REQUEST_ATTRIBUTES_BY_TYPE = "SELECT A4.id AS id, A4.name AS name, A3.name AS type, attr_order AS order, multiple, mandatory, immutable " +
            "FROM (attributes AS A1 INNER JOIN request_types_attributes AS A2 ON A1.id=A2.attr_id) AS A4 INNER JOIN attribute_types AS A3 ON A3.id=A4.attr_type_id " +
            "WHERE req_type_id=(SELECT id FROM request_types WHERE name=? ORDER by creation_date DESC LIMIT 1)";
    public static final String RESERVED_VALUES_FOR_ATTRIBUTE_ID = "SELECT value_content " +
            "FROM attributes_res_values AS A1 INNER JOIN reserved_values AS R1 ON A1.value_id=R1.id " +
            "WHERE attr_id=?";
    public static final String GET_ALL_WAREHOUSES = "SELECT * FROM warehouses";
    public static final String GET_ALL_EQUIPMENT = "SELECT * FROM equipment";
    public static final String GET_LIMITED_EQUIPMENT = "SELECT * FROM equipment LIMIT ?";
    public static final String ADD_REQUEST_ATTRIBUTE = "INSERT INTO request_values(request_id, attr_id, string_value) " +
            "VALUES (?, ?, ?)";
    public static final String ADD_REQUEST_EQUIPMENT = "INSERT INTO request_equipment(request_id, equipment_id, quantity) " +
            "VALUES (?, ?, ?)";
    public static final String FIND_EQUIPMENT_BY_VALUE = "SELECT * FROM equipment " +
            "WHERE (model LIKE ?) OR (producer LIKE ?) OR (country LIKE ?) LIMIT 50";
    public static final String ARCHIVE_OLD_REQUESTS = "UPDATE requests" +
            " SET archive = true" +
            " WHERE (status = 'Completed' OR status = 'Canceled') AND modified_date <= ?";
}