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
    public static final String ADD_NEW_REQUEST = "INSERT INTO requests " +
            "(id, creator_id, req_type_id, status, creation_date, modified_date, description, archive) VALUES (?,?,?,?,?,?,?,?)";
    public static final String CHANGE_REQUEST_STATUS_AND_CREATOR_ID = "UPDATE requests " +
            "SET creator_id = ?, status = ?, modified_date = ?" +
            "WHERE id = ?";
    public static final String CHANGE_REQUEST_STATUS = "UPDATE requests" +
            " SET status = ?, modified_date = ?" +
            " WHERE id = ?";
    public static final String CHANGE_REQUEST_STATUS_AND_EXECUTOR_ID = "UPDATE requests " +
            "SET executor_id = ?, status = ?, modified_date = ?" +
            "WHERE id = ?";
    public static final String REQUEST_INFO_BY_ID = "SELECT warehouse_id, creator_id, executor_id, type_id," +
            " title, status, creation_date, modified_date, description, archive" +
            "FROM requests" +
            "WHERE requests.id = ?";
    public static final String REQUEST_ATTRIBUTES_BY_ID = "SELECT attributes" +
            "FROM requests" +
            "WHERE requests.id = ?";
    public static final String ARCHIVE_OLD_REQUESTS = "UPDATE requests" +
            " SET archive = true" +
            " WHERE (status = 'Completed' OR status = 'Canceled') AND modified_date <= ?";
    public static final String GET_REGISTRATION_EMAIL_TEMPLATE = "SELECT template FROM email_templates WHERE id = 'Registration'";
    public static final String GET_REQUEST_STATUS_CHANGED_EMAIL_TEMPLATE = "SELECT template FROM email_templates WHERE id = 'Request_status_changed'";
    public static final String GET_USER_ROLES_CHANGED_EMAIL_TEMPLATE = "SELECT template FROM email_templates WHERE id = 'User_roles_changed'";
}
