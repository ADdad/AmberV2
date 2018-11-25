package amber_team.amber.model.dto;

public class ReportOrdersWithUserDto extends ReportOrdersDto {
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
