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
}
