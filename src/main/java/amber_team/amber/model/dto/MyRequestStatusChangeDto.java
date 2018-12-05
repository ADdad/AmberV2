package amber_team.amber.model.dto;

public class MyRequestStatusChangeDto {
    private String requestId;
    private String executorId;
    private String status;
    private String userId;
    private String commentText;

    public String getConnectedRequest() {
        return connectedRequest;
    }

    public void setConnectedRequest(String connectedRequest) {
        this.connectedRequest = connectedRequest;
    }

    private String connectedRequest;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commmentText) {
        this.commentText = commmentText;
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
