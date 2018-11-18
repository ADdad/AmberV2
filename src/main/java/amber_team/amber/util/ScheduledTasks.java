package amber_team.amber.util;

import amber_team.amber.service.interfaces.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class ScheduledTasks {
    @Autowired
    private RequestService requestService;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Scheduled(cron="0 1 0 ? * *")
    public void reportCurrentTime() {
        log.info("Archiving old requests for {}", dateFormat.format(new Date()));
        Calendar tenDaysBefore = Calendar.getInstance();
        tenDaysBefore.setTime(new Date());
        tenDaysBefore.add(Calendar.DAY_OF_MONTH, -10);
        requestService.archiveOldRequests(tenDaysBefore.getTime());
        log.info("Archiving ended on {}", dateFormat.format(new Date()));
    }
}
