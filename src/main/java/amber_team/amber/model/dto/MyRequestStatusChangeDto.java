package amber_team.amber.model.dto;

public class MyRequestStatusChangeDto {
    private String requestId;
    private String executorId;
    private String status;
    private String userId;
    private String commmentText;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommmentText() {
        return commmentText;
    }

    public void setCommmentText(String commmentText) {
        this.commmentText = commmentText;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String id) {
        this.requestId = id;
    }

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
