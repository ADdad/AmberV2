package amber_team.amber.model.dto;

import amber_team.amber.util.Status;

import java.util.List;

public class ListRequestChangeStatusDto {
    private Status status;
    private List<String> requests;

    public ListRequestChangeStatusDto() {
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

    public List<String> getRequests() {
        return requests;
    }

    public void setRequests(List<String> requests) {
        this.requests = requests;
    }
}
