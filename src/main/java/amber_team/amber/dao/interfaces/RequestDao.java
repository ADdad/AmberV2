package amber_team.amber.dao.interfaces;


import amber_team.amber.model.entities.Request;

import java.util.List;

public interface RequestDao {
    public Request create(Request request);

    public Request update(Request request);

    public Request getByRequest(Request requestId);

    public Request getById(String requestId);

    int getCountOfUsersActiveRequests(String userId);

    List<Request> getAllUsersRequests(String userId);

    List<Request> getAllUsersRequestsPagination(String userId, int offset, int limit);

    public void archiveOldRequests();

    List<Request> getAdminRequestsPagination(String id, int i, int pagination);

    List<Request> getKeeperRequestsPagination(String id, int i, int pagination);

    int getCountOfKeeperActiveRequests(String userId);

    int getCountOfAdminActiveRequests();
}
