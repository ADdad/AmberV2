package amber_team.amber.util;

public class SQLQueries {
    public static final String ADD_NEW_USER_AND_HIS_ROLE = "BEGIN; INSERT INTO users" +
            " (id ,email, password, s_name, f_name) VALUES (?, ?, ?, ?, ?);" +
            "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?); COMMIT;";
    public static final String EXISTING_THIS_EMAIL = "SELECT email FROM users WHERE email=?";
}
