package amber_team.amber.dao.interfaces;

public interface EmailDao {
    public String getRegistrationTemplate();
    public String getRequestStatusChangedTemplate();
    public String getUserRolesChangedTemplate();

    public String getRequestCreatedTemplate();
}
