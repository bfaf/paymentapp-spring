package com.kchonov.springdocker.cron;

import com.kchonov.springdocker.entity.constants.Misc;
import java.util.Calendar;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Cleanup {

    Logger logger = LoggerFactory.getLogger(Cleanup.class);

    @Autowired
    private List<ICleanup> list;

    @Scheduled(cron = "0 0 * * * *")
    public void run() {
        if (list.isEmpty()) {
            logger.error("No classes implement cleanup functionality");
        }
        long oneHourOld = Calendar.getInstance().getTimeInMillis() - Misc.ONE_HOUR;
        list.forEach(c -> {
            c.clean(oneHourOld);
        });
    }
}
