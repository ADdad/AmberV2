package amber_team.amber.model;


public class RequestStatusChangeDto {

    private String username;
    private String request_id;
    private String creator_id;
    private String executor_id;
    private String status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String id) {
        this.request_id = id;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getExecutor_id() {
        return executor_id;
    }

    public void setExecutor_id(String executor_id) {
        this.executor_id = executor_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
