package amber_team.amber.util;

public class SQLQueries {

    public static final String DROP_USER_ROLES = "DELETE FROM user_roles";

    private SQLQueries() { }

    public static final String EQUIPMENT_INFO_BY_WAREHOUSE = "SELECT equipment.id, equipment.model, equipment.producer,"+
        "equipment.country, warehouse_equipment.quantity "+
            "FROM equipment " +
            "INNER JOIN warehouse_equipment ON equipment.id = warehouse_equipment.equipment_id"+
            "WHERE warehouse_equipment.warehouse_id = ?";
    public static final String ADD_NEW_USER_AND_HIS_ROLE = "BEGIN; INSERT INTO users" +
            " (id ,email, password, s_name, f_name, enabled) VALUES (?, ?, ?, ?, ?, ?);" +
            "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?); COMMIT;";
    public static final String EXISTING_THIS_EMAIL = "SELECT email FROM users WHERE email = ?";
    public static final String USER_BY_USERNAME_QUERY = "SELECT users.email as username, " +
            "users.password as password, enabled " +
            "FROM users WHERE users.email = ?";
    public static final String CHANGE_USERS_AND_THEIR_ROLES = "INSERT INTO user_roles (user_id, role_id) VALUES (?,?)";
    public static final String AUTHORITIES_BY_USERNAME = "SELECT users.email as username, roles.name as role " +
            "FROM users " +
            "INNER JOIN user_roles ON users.id = user_roles.user_id " +
            "INNER JOIN roles ON user_roles.role_id = roles.id " +
            "WHERE users.email = ?";
    public static final String USER_INFO_BY_USERNAME = "SELECT id, email, f_name, s_name " +
            "FROM users WHERE users.email =?";
    public static final String IF_EXISTS = "SELECT count(*) FROM user_roles WHERE  user_id = ? AND role_id = ?";
    public static final String USERS_INFO = "SELECT users.id, users.email, users.f_name, users.s_name, roles.name " +
            "FROM users " +
            "INNER JOIN user_roles ON users.id = user_roles.user_id " +
            "INNER JOIN roles ON user_roles.role_id = roles.id ";
    public static final String USER_ROLES_BY_ID = "SELECT roles.id, roles.name " +
            "FROM roles " +
            "INNER JOIN user_roles ON roles.id = user_roles.role_id " +
            "WHERE user_roles.user_id = ?";
    public static final String TYPE_BY_ID = "SELECT * " +
            "FROM request_types " +
            "WHERE id = ? ORDER BY creation_date DESC LIMIT 1";
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
            "WHERE request_id = ? ORDER BY creation_date DESC";
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
    public static final String GET_AVAILABLE_EQUIPMENT_WITH_PAGINATION = "SELECT * " +
            " FROM (SELECT equipment.id AS equipmentId, warehouse_equipment.quantity AS quantity," +
                " equipment.model AS model, equipment.producer AS producer, equipment.country AS country," +
                " warehouses.id" +
                " FROM equipment" +
                " INNER JOIN warehouse_equipment ON equipment.id = warehouse_equipment.equipment_id" +
                " INNER JOIN warehouses ON warehouses.id = warehouse_equipment.warehouse_id" +
                " WHERE warehouses.id =? AND warehouse_equipment.quantity > 0" +
            " ) AS sub" +
            " ORDER BY sub.equipmentId" +
            " LIMIT ?" +
            " OFFSET ?";
    public static final String GET_ALL_AVAILABLE_EQUIPMENT = "SELECT equipment.id AS equipmentId, warehouse_equipment.quantity AS quantity," +
            " equipment.model AS model, equipment.producer AS producer, equipment.country AS country," +
            " warehouses.id" +
            " FROM equipment" +
            " INNER JOIN warehouse_equipment ON equipment.id = warehouse_equipment.equipment_id" +
            " INNER JOIN warehouses ON warehouses.id = warehouse_equipment.warehouse_id" +
            " WHERE warehouses.id =? AND warehouse_equipment.quantity > 0" +
            " ORDER BY equipment.id";
    public static final String GET_NONAVAILABLE_EQUIPMENT_WITH_PAGINATION = "SELECT * " +
            " FROM (SELECT equipment.id AS equipmentId, warehouse_equipment.quantity AS quantity," +
                " equipment.model AS model, equipment.producer AS producer, equipment.country AS country" +
                " FROM equipment" +
                " INNER JOIN warehouse_equipment ON equipment.id = warehouse_equipment.equipment_id" +
                " INNER JOIN warehouses ON warehouses.id = warehouse_equipment.warehouse_id" +
                " WHERE warehouse_equipment.warehouse_id =? AND warehouse_equipment.quantity < 1" +
            " ) AS sub" +
            " ORDER BY sub.equipmentId" +
            " LIMIT ?" +
            " OFFSET ?";
    public static final String GET_ALL_NONAVAILABLE_EQIPMENT = "SELECT equipment.id AS equipmentId, warehouse_equipment.quantity AS quantity," +
            " equipment.model AS model, equipment.producer AS producer, equipment.country AS country" +
            " FROM equipment" +
            " INNER JOIN warehouse_equipment ON equipment.id = warehouse_equipment.equipment_id" +
            " INNER JOIN warehouses ON warehouses.id = warehouse_equipment.warehouse_id" +
            " WHERE warehouse_equipment.warehouse_id =? AND warehouse_equipment.quantity < 1" +
            " ORDER BY equipment.id";
    public static final String GET_DELIVERED_EQUIPMENT_WITH_PAGINATION = "SELECT * " +
            " FROM (SELECT equipment.id AS equipmentId, SUM(request_equipment.quantity) AS quantity, equipment.model AS model," +
                " equipment.producer AS producer, equipment.country AS country " +
                " FROM request_equipment" +
                " INNER JOIN equipment ON equipment.id = request_equipment.equipment_id" +
                " INNER JOIN requests ON requests.id = request_equipment.request_id" +
                " INNER JOIN request_types ON requests.req_type_id = request_types.id" +
                " WHERE requests.warehouse_id =? AND requests.status = 'Completed' AND request_types.name='order'" +
                " AND requests.modified_date >= ? AND requests.modified_date <= ?" +
                " GROUP BY equipment.id" +
            " ) AS sub " +
            " ORDER BY sub.equipmentId " +
            " LIMIT ? " +
            " OFFSET ?";
    public static final String GET_ALL_DELIVERED_EQUIPMENT = "SELECT equipment.id AS equipmentId, SUM(request_equipment.quantity) AS quantity, equipment.model AS model," +
            " equipment.producer AS producer, equipment.country AS country " +
            " FROM request_equipment" +
            " INNER JOIN equipment ON equipment.id = request_equipment.equipment_id" +
            " INNER JOIN requests ON requests.id = request_equipment.request_id" +
            " INNER JOIN request_types ON requests.req_type_id = request_types.id" +
            " WHERE requests.warehouse_id =? AND requests.status = 'Completed' AND request_types.name='order'" +
            " AND requests.modified_date >= ? AND requests.modified_date <= ?" +
            " GROUP BY equipment.id " +
            " ORDER BY equipment.id";
    public static final String GET_ENDING_EQUIPMENT_WITH_PAGINATION = "SELECT *" +
            " FROM (SELECT equipment.id AS equipmentId, equipment.producer AS producer, equipment.country AS country," +
            " equipment.model AS model, warehouse_equipment.quantity AS quantity" +
            " FROM equipment" +
            " INNER JOIN warehouse_equipment ON warehouse_equipment.equipment_id = equipment.id" +
            " WHERE warehouse_equipment.warehouse_id = ? AND warehouse_equipment.quantity < ?" +
            " ) AS sub" +
            " ORDER BY sub.equipmentId" +
            " LIMIT ?" +
            " OFFSET ?";
    public static final String GET_ALL_ENDING_EQUIPMENT = "SELECT equipment.id AS equipmentId, equipment.producer AS producer, equipment.country AS country," +
            " equipment.model AS model, warehouse_equipment.quantity AS quantity" +
            " FROM equipment" +
            " INNER JOIN warehouse_equipment ON warehouse_equipment.equipment_id = equipment.id" +
            " WHERE warehouse_equipment.warehouse_id = ? AND warehouse_equipment.quantity < ?" +
            " ORDER BY equipment.id";
    public static final String DELETE_REQUEST_EQUIPMENT = "DELETE FROM request_equipment WHERE request_id = ?";
    public static final String DELETE_REQUEST_VALUES = "DELETE FROM request_values WHERE request_id = ?";
    public static final String GET_PROCESSED_ORDERS_WITH_PAGINATION = "SELECT *" +
            " FROM ( SELECT requests.id AS requestId, requests.creation_date AS creation_date, requests.status AS status, requests.title AS title, " +
                " requests.modified_date AS modified_date, requests.description AS description, request_types.name AS order_type," +
                " creators.f_name AS creator_name, creators.s_name AS creator_surname, creators.email AS creator_email," +
                " executors.f_name AS executor_name, executors.s_name AS executor_surname, executors.email AS executor_email," +
                " warehouses.adress AS warehouse_address, warehouses.contact_number AS warehouse_phone" +
                " FROM requests" +
                " INNER JOIN users AS creators ON requests.creator_id=creators.id" +
                " LEFT OUTER JOIN users AS executors ON requests.executor_id=executors.id" +
                " INNER JOIN warehouses ON requests.warehouse_id=warehouses.id" +
                " INNER JOIN request_types ON requests.req_type_id=request_types.id" +
                " WHERE (requests.status='Completed' OR requests.status='Rejected') AND requests.modified_date >= ?" +
                " AND requests.modified_date <= ?" +
            " ) AS sub" +
            " ORDER BY sub.requestId" +
            " LIMIT ?" +
            " OFFSET ?";
    public static final String GET_ALL_PROCESSED_ORDERS = "SELECT requests.id AS requestId, requests.creation_date AS creation_date, requests.status AS status, requests.title AS title, " +
            " requests.modified_date AS modified_date, requests.description AS description, request_types.name AS order_type," +
            " creators.f_name AS creator_name, creators.s_name AS creator_surname, creators.email AS creator_email," +
            " executors.f_name AS executor_name, executors.s_name AS executor_surname, executors.email AS executor_email," +
            " warehouses.adress AS warehouse_address, warehouses.contact_number AS warehouse_phone" +
            " FROM requests" +
            " INNER JOIN users AS creators ON requests.creator_id=creators.id" +
            " LEFT OUTER JOIN users AS executors ON requests.executor_id=executors.id" +
            " INNER JOIN warehouses ON requests.warehouse_id=warehouses.id" +
            " INNER JOIN request_types ON requests.req_type_id=request_types.id" +
            " WHERE (requests.status='Completed' OR requests.status='Rejected') AND requests.modified_date >= ?" +
            " AND requests.modified_date <= ? " +
            " ORDER BY requests.id";
    public static final String GET_UNPROCESSED_ORDERS_WITH_PAGINATION = "SELECT *" +
            " FROM ( SELECT requests.id AS requestId, requests.creation_date AS creation_date, requests.status AS status, requests.title AS title, " +
                " requests.modified_date AS modified_date, requests.description AS description, request_types.name AS order_type," +
                " creators.f_name AS creator_name, creators.s_name AS creator_surname, creators.email AS creator_email," +
                " executors.f_name AS executor_name, executors.s_name AS executor_surname, executors.email AS executor_email," +
                " warehouses.adress AS warehouse_address, warehouses.contact_number AS warehouse_phone" +
                " FROM requests" +
                " INNER JOIN users AS creators ON requests.creator_id=creators.id" +
                " LEFT OUTER JOIN users AS executors ON requests.executor_id=executors.id" +
                " INNER JOIN warehouses ON requests.warehouse_id=warehouses.id" +
                " INNER JOIN request_types ON requests.req_type_id=request_types.id" +
                " WHERE requests.status!='Completed' AND requests.status!='Rejected' AND requests.modified_date >= ?" +
                " AND requests.modified_date <= ?" +
            " ) AS sub" +
            " ORDER BY sub.requestId" +
            " LIMIT ?" +
            " OFFSET ?";
    public static final String GET_ALL_UNPROCESSED_ORDERS = "SELECT requests.id AS requestId, requests.creation_date AS creation_date, requests.status AS status, requests.title AS title, " +
            " requests.modified_date AS modified_date, requests.description AS description, request_types.name AS order_type," +
            " creators.f_name AS creator_name, creators.s_name AS creator_surname, creators.email AS creator_email," +
            " executors.f_name AS executor_name, executors.s_name AS executor_surname, executors.email AS executor_email," +
            " warehouses.adress AS warehouse_address, warehouses.contact_number AS warehouse_phone" +
            " FROM requests" +
            " INNER JOIN users AS creators ON requests.creator_id=creators.id" +
            " LEFT OUTER JOIN users AS executors ON requests.executor_id=executors.id" +
            " INNER JOIN warehouses ON requests.warehouse_id=warehouses.id" +
            " INNER JOIN request_types ON requests.req_type_id=request_types.id" +
            " WHERE requests.status!='Completed' AND requests.status!='Rejected' AND requests.modified_date >= ?" +
            " AND requests.modified_date <= ? " +
            " ORDER BY requests.id";
    public static final String GET_EXECUTED_ORDERS_BY_WITH_PAGINATION = "SELECT *" +
            " FROM ( SELECT requests.id AS requestId, requests.creation_date AS creation_date, requests.status AS status, requests.title AS title, " +
                " requests.modified_date AS modified_date, requests.description AS description, request_types.name AS order_type," +
                " creators.f_name AS creator_name, creators.s_name AS creator_surname, creators.email AS creator_email," +
                " executors.f_name AS executor_name, executors.s_name AS executor_surname, executors.email AS executor_email," +
                " warehouses.adress AS warehouse_address, warehouses.contact_number AS warehouse_phone" +
                " FROM requests" +
                " INNER JOIN users AS creators ON requests.creator_id=creators.id" +
                " INNER JOIN users AS executors ON requests.executor_id=executors.id" +
                " INNER JOIN warehouses ON requests.warehouse_id=warehouses.id" +
                " INNER JOIN request_types ON requests.req_type_id=request_types.id" +
                " WHERE requests.status='Completed' AND requests.modified_date >= ? AND requests.modified_date <= ?" +
                " AND requests.executor_id=?" +
            " ) AS sub" +
            " ORDER BY sub.requestId" +
            " LIMIT ?" +
            " OFFSET ?";
    public static final String GET_ALL_EXECUTED_ORDERS = "SELECT requests.id AS requestId, requests.creation_date AS creation_date, requests.status AS status, requests.title AS title, " +
            " requests.modified_date AS modified_date, requests.description AS description, request_types.name AS order_type," +
            " creators.f_name AS creator_name, creators.s_name AS creator_surname, creators.email AS creator_email," +
            " executors.f_name AS executor_name, executors.s_name AS executor_surname, executors.email AS executor_email," +
            " warehouses.adress AS warehouse_address, warehouses.contact_number AS warehouse_phone" +
            " FROM requests" +
            " INNER JOIN users AS creators ON requests.creator_id=creators.id" +
            " INNER JOIN users AS executors ON requests.executor_id=executors.id" +
            " INNER JOIN warehouses ON requests.warehouse_id=warehouses.id" +
            " INNER JOIN request_types ON requests.req_type_id=request_types.id" +
            " WHERE requests.status='Completed' AND requests.modified_date >= ? AND requests.modified_date <= ?" +
            " AND requests.executor_id=?" +
            " ORDER BY requests.id";
    public static final String GET_CREATED_ORDERS_BY_WITH_PAGINATION = "SELECT *" +
            " FROM ( SELECT requests.id AS requestId, requests.creation_date AS creation_date, requests.status AS status, requests.title AS title, " +
                " requests.modified_date AS modified_date, requests.description AS description, request_types.name AS order_type," +
                " creators.f_name AS creator_name, creators.s_name AS creator_surname, creators.email AS creator_email," +
                " executors.f_name AS executor_name, executors.s_name AS executor_surname, executors.email AS executor_email," +
                " warehouses.adress AS warehouse_address, warehouses.contact_number AS warehouse_phone" +
                " FROM requests" +
                " INNER JOIN users AS creators ON requests.creator_id=creators.id" +
                " LEFT OUTER JOIN users AS executors ON requests.executor_id=executors.id" +
                " INNER JOIN warehouses ON requests.warehouse_id=warehouses.id" +
                " INNER JOIN request_types ON requests.req_type_id=request_types.id" +
                " WHERE requests.creation_date >= ? AND requests.creation_date <= ?" +
                " AND requests.creator_id=?" +
            " ) AS sub" +
            " ORDER BY sub.requestId" +
            " LIMIT ?" +
            " OFFSET ?";
    public static final String GET_ALL_CREATED_ORDERS = "SELECT requests.id AS requestId, requests.creation_date AS creation_date, requests.status AS status, requests.title AS title, " +
            " requests.modified_date AS modified_date, requests.description AS description, request_types.name AS order_type," +
            " creators.f_name AS creator_name, creators.s_name AS creator_surname, creators.email AS creator_email," +
            " executors.f_name AS executor_name, executors.s_name AS executor_surname, executors.email AS executor_email," +
            " warehouses.adress AS warehouse_address, warehouses.contact_number AS warehouse_phone" +
            " FROM requests" +
            " INNER JOIN users AS creators ON requests.creator_id=creators.id" +
            " LEFT OUTER JOIN users AS executors ON requests.executor_id=executors.id" +
            " INNER JOIN warehouses ON requests.warehouse_id=warehouses.id" +
            " INNER JOIN request_types ON requests.req_type_id=request_types.id" +
            " WHERE requests.creation_date >= ? AND requests.creation_date <= ?" +
            " AND requests.creator_id=?" +
            " ORDER BY requests.id";
    public static final String GET_WAREHOUSES_WITH_PAGINATION = "SELECT * " +
            " FROM ( SELECT warehouses.id AS id, warehouses.adress AS address" +
                " FROM warehouses" +
            " ) AS sub" +
            " ORDER BY sub.id " +
            " LIMIT ?" +
            " OFFSET ?";
    public static final String GET_EXECUTORS_WITH_PAGINATION = "SELECT * " +
            " FROM ( SELECT DISTINCT requests.executor_id AS id, users.f_name AS name, users.s_name AS surname" +
                " FROM requests" +
                " INNER JOIN users ON requests.executor_id=users.id" +
            " ) AS sub" +
            " ORDER BY sub.id" +
            " LIMIT ?" +
            " OFFSET ?";
    public static final String GET_CREATORS_WITH_PAGINATION = "SELECT * " +
            " FROM ( SELECT DISTINCT requests.creator_id AS id, users.f_name AS name, users.s_name AS surname" +
                " FROM requests" +
                " INNER JOIN users ON requests.creator_id=users.id" +
            " ) AS sub" +
            " ORDER BY sub.id" +
            " LIMIT ?" +
            " OFFSET ?";
    public static final String DELETE_USER_ROLES = "DELETE FROM user_roles WHERE user_id = ?";
    public static final String ADD_USER_ROLE = "INSERT INTO user_roles VALUES (?, ?)";
    public static final String GET_USERS_WITH_PAGINATION = "SELECT id, f_name, s_name, password, email, enabled " +
            "FROM users LIMIT ? OFFSET ?";
    public static final String GET_ALL_ROLES = "SELECT * FROM roles";
    public static final String GET_ALL_ACTIVE_USERS = "SELECT * FROM users WHERE enabled = 1";
    public static final String GET_ALL_USERS = "SELECT * FROM users";

}
