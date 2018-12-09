package amber_team.amber.util;

import amber_team.amber.service.interfaces.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledTasks {

    private final RequestService requestService;

    @Autowired
    public ScheduledTasks(RequestService requestService) {
        this.requestService = requestService;
    }

    @Scheduled(cron = "0 1 0 ? * *")
    public void archiveOldRequests() {
        requestService.archiveOldRequests();
    }
}
