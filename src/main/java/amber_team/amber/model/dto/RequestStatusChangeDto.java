package amber_team.amber.model.dto;

import amber_team.amber.util.Status;

public class RequestStatusChangeDto {
    private String requestId;
    private String executorId;
    private Status status;
    private String userId;
    private String commentText;

    public RequestStatusChangeDto() {
    }

    public RequestStatusChangeDto(String requestId, Status status) {
        this.requestId = requestId;
        this.status = status;
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = Status.valueOfStatus(status);
    }
}
