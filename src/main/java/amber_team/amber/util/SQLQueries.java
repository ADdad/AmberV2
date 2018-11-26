package amber_team.amber.util;

public class SQLQueries {
    private SQLQueries() {
    }

    public static final String ADD_NEW_USER_AND_HIS_ROLE = "BEGIN; INSERT INTO users" +
            " (id ,email, password, s_name, f_name, enabled) VALUES (?, ?, ?, ?, ?, ?);" +
            "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?); COMMIT;";
    public static final String EXISTING_THIS_EMAIL = "SELECT email FROM users WHERE email=?";
    public static final String USER_BY_USERNAME_QUERY = "SELECT users.email as username, " +
            "users.password as password, enabled " +
            "FROM users WHERE users.email=?";
    public static final String CHANGE_USERS_AND_THEIR_ROLES = "";
    public static final String AUTHORITIES_BY_USERNAME = "SELECT users.email as username, roles.name as role " +
            "FROM users " +
            "INNER JOIN user_roles ON users.id = user_roles.user_id " +
            "INNER JOIN roles ON user_roles.role_id = roles.id " +
            "WHERE users.email=?";
    public static final String USER_INFO_BY_USERNAME = "SELECT id, email, f_name, s_name " +
            "FROM users WHERE users.email=?";

//    public static final String USERS_INFO = "SELECT users.id, users.email, users.f_name, users.s_name " +
//            "FROM users ";
    public static final String USERS_INFO = "SELECT users.id, users.email, users.f_name, users.s_name, roles.name " +
            "FROM users " +
            "INNER JOIN user_roles ON users.id = user_roles.user_id " +
            "INNER JOIN roles ON user_roles.role_id = roles.id ";
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
            "(id, creator_id, req_type_id, status, creation_date, modified_date, description, archive, warehouse_id," +
            " title) VALUES (?,?,?,?,?,?,?,?,?,?)";
    public static final String REQUEST_INFO_BY_ID = "SELECT warehouse_id, creator_id, executor_id, req_type_id," +
            " title, status, creation_date, modified_date, description, archive, connected_request " +
            "FROM requests " +
            "WHERE requests.id = ?";
    public static final String REQUEST_ATTRIBUTES_BY_TYPE = "SELECT A4.id AS id, A4.name AS name, A3.name AS type," +
            " attr_order AS order, multiple, mandatory, immutable " +
            "FROM (attributes AS A1 INNER JOIN request_types_attributes AS A2 ON A1.id=A2.attr_id) AS A4" +
            " INNER JOIN attribute_types AS A3 ON A3.id=A4.attr_type_id " +
            "WHERE req_type_id=(SELECT id FROM request_types WHERE name=? ORDER by creation_date DESC LIMIT 1)";
    public static final String RESERVED_VALUES_FOR_ATTRIBUTE_ID = "SELECT value_content " +
            "FROM attributes_res_values AS A1 INNER JOIN reserved_values AS R1 ON A1.value_id=R1.id " +
            "WHERE attr_id=?";
    public static final String GET_ALL_WAREHOUSES = "SELECT * FROM warehouses";
    public static final String GET_ALL_EQUIPMENT = "SELECT * FROM equipment";
    public static final String GET_LIMITED_EQUIPMENT = "SELECT * FROM equipment LIMIT ?";
    public static final String GET_EQUIPMENT_BY_ID = "SELECT * FROM equipment WHERE id = ?";
    public static final String ADD_REQUEST_ATTRIBUTE = "INSERT INTO request_values(request_id, attr_id, string_value," +
            " date_value, decimal_value) " +
            "VALUES (?, ?, ?, ?, ?)";
    public static final String ADD_REQUEST_EQUIPMENT = "INSERT INTO request_equipment(request_id, equipment_id, quantity) " +
            "VALUES (?, ?, ?)";
    public static final String FIND_EQUIPMENT_BY_VALUE = "SELECT * FROM equipment " +
            "WHERE (model LIKE ?) OR (producer LIKE ?) OR (country LIKE ?) LIMIT 50";
    public static final String ARCHIVE_OLD_REQUESTS = "UPDATE requests" +
            " SET archive = true" +
            " WHERE (status = 'Completed' OR status = 'Canceled') AND modified_date <= (CURRENT_DATE - interval '10 days')";
    public static final String GET_REGISTRATION_EMAIL_TEMPLATE = "SELECT template FROM email_templates "
            + "WHERE id = 'Registration'";
    public static final String GET_REQUEST_STATUS_CHANGED_EMAIL_TEMPLATE = "SELECT template FROM email_templates " +
            "WHERE id = 'Request_status_changed'";
    public static final String GET_USER_ROLES_CHANGED_EMAIL_TEMPLATE = "SELECT template FROM email_templates " +
            "WHERE id = 'User_roles_changed'";
    public static final String GET_WAREHOUSE_BY_ID = "SELECT id, adress, contact_number FROM warehouses WHERE id = ?";
    public static final String GET_COMMENTS_OF_REQUEST = "SELECT id, user_id, comment_text, creation_date FROM comments " +
            "WHERE request_id = ?";
    public static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    public static final String REQUEST_ATTRIBUTES_VALUES_BY_ID = "SELECT attr_id AS id, A1.name AS name, A1.attr_order," +
            " A1.type AS type, string_value, date_value, decimal_value " +
            "FROM request_values AS RV1 INNER JOIN (SELECT A2.id AS id, A2.name AS name, AT1.name AS type, " +
            "A2.attr_order AS attr_order FROM attributes AS A2 INNER JOIN attribute_types AS AT1 " +
            "ON A2.attr_type_id=AT1.id) AS A1 ON RV1.attr_id=A1.id " +
            "WHERE RV1.request_id = ?";
    public static final String ADD_WAREHOUSE_EQUIPMENT = "INSERT INTO warehouse_equipment(warehouse_id, equipment_id, quantity) " +
            "VALUES (?, ?, ?)";
    public static final String GET_REQUEST_EQUIPMENT = "SELECT E1.id AS id, E1.model AS model, E1.producer AS producer," +
            " E1.country AS country, E2.quantity AS quantity " +
            "FROM equipment AS E1 INNER JOIN request_equipment AS E2 on E1.id = E2.equipment_id " +
            "WHERE request_id = ?";
    public static final String GET_REQUEST_WAREHOUSE_EQUIPMENT_QUANTITY_DIFFERENCE = "SELECT e.id AS id, " +
            "  re.quantity-we.quantity AS quantity_diff " +
            "FROM request_equipment AS re " +
            "INNER JOIN equipment AS e ON e.id = re.equipment_id " +
            "INNER JOIN warehouse_equipment AS we ON e.id = we.equipment_id " +
            "WHERE request_id = ?";
    public static final String GET_WAREHOUSE_EXECUTORS = "SELECT DISTINCT id, f_name, s_name, email " +
            "FROM user_warehouses uw INNER JOIN (users AS U1 INNER JOIN user_roles u on U1.id = u.user_id) AS u2 "
            + "ON uw.user_id=u2.user_id " +
            "WHERE role_id = (SELECT id FROM roles WHERE name = 'ROLE_KEEPER') AND uw.warehouse_id = ?";
    public static final String UPDATE_REQUEST = "UPDATE requests SET warehouse_id = ?, creator_id = ?, executor_id = ?,"
            + " req_type_id = ?, connected_request = ?, title = ?, status = ?, creation_date = ?, modified_date = ?," +
            " description = ?, archive = ? " +
            "WHERE id = ?";
    public static final String ADD_NEW_COMMENT = "INSERT INTO comments VALUES (?, ?, ?, ?, ?)";

}
