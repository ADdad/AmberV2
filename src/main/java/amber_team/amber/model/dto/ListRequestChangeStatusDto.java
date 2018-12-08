package amber_team.amber.model.dto;

import java.util.List;

public class ListRequestChangeStatusDto {
    private String status;
    private List<String> requests;

    public ListRequestChangeStatusDto() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getRequests() {
        return requests;
    }

    public void setRequests(List<String> requests) {
        this.requests = requests;
    }
}
