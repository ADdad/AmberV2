package amber_team.amber.util;

import java.util.regex.Pattern;

public class RegExp {
    public static final Pattern EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

}
