package amber_team.amber.dao.interfaces;

public interface EmailDao {
    String getRegistrationTemplate();

    String getRequestStatusChangedTemplate();

    String getUserRolesChangedTemplate();

    String getRequestCreatedTemplate();
}
