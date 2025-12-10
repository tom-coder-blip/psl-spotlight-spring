package net.javaguides.pslspotlightspring.jobs;

import net.javaguides.pslspotlightspring.services.TrendingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TrendingScheduler {

    private final TrendingService trendingService;

    public TrendingScheduler(TrendingService trendingService) {
        this.trendingService = trendingService;
    }

    // Run every night at midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void nightlyDecay() {
        System.out.println("Running nightly trending decay...");
        trendingService.applyDecay();
    }
}
