package amber_team.amber.dao.interfaces;


import amber_team.amber.model.entities.Request;

import java.util.Collection;
import java.util.List;

public interface RequestDao {
    Request create(Request request);

    Request update(Request request);

    Request getById(String requestId);

    int getCountOfUsersRequests(String userId, boolean archive);

    List<Request> getAllUsersRequestsPagination(String userId, int offset, int limit, boolean archive);

    void archiveOldRequests();

    List<Request> getAdminRequestsPagination(String id, int i, int pagination, boolean archive);

    List<Request> getKeeperRequestsPagination(String id, int i, int pagination, boolean archive);

    int getCountOfKeeperRequests(String userId, boolean archive);

    int getCountOfAdminRequests(boolean archive);

    List<Request> searchRequests(String search, String id, boolean archive);

    List<Request> searchExecutorRequests(String search, String id, boolean archive);

    List<Request> searchAdminRequests(String search, boolean archive);
}
