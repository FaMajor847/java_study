package org.mav.example.limits.sheduler;

import org.mav.example.limits.service.LimitService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class LimitsResetScheduler {

    private static final Logger log = LoggerFactory.getLogger(LimitsResetScheduler.class);
    private final LimitService limitService;

    public LimitsResetScheduler(LimitService limitService) {
        this.limitService = limitService;
    }

    /** Каждый день в полночь готовим лимиты на новый день. */
    @Scheduled(cron = "0 0 0 * * *")
    public void resetDailyLimits() {
        log.info("Resetting daily limits...");
        limitService.prepareTodayForAllKnownClients();
        log.info("Daily limits prepared.");
    }
}
