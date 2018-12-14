package amber_team.amber.dao.interfaces;


import amber_team.amber.model.entities.Request;

import java.util.List;

public interface RequestDao {
    Request create(Request request);

    Request update(Request request);

    Request getByRequest(Request requestId);

    Request getById(String requestId);

    int getCountOfUsersActiveRequests(String userId);

    List<Request> getAllUsersRequests(String userId);

    List<Request> getAllUsersRequestsPagination(String userId, int offset, int limit);

    void archiveOldRequests();

    List<Request> getAdminRequestsPagination(String id, int i, int pagination);

    List<Request> getKeeperRequestsPagination(String id, int i, int pagination);

    int getCountOfKeeperActiveRequests(String userId);

    int getCountOfAdminActiveRequests();
}
