package amber_team.amber.model.dto;

import amber_team.amber.model.entities.Request;

import java.util.List;

public class RequestListDtoPagination {

    private List<Request> requests;
    private int requestsCount;

    public int getRequestsCount() {
        return requestsCount;
    }

    public void setRequestsCount(int requestsCount) {
        this.requestsCount = requestsCount;
    }

    public RequestListDtoPagination() {
    }

    public RequestListDtoPagination(List<Request> requests) {
        this.requests = requests;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}
