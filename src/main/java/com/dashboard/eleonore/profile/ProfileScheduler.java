package com.dashboard.eleonore.profile;

import com.dashboard.eleonore.profile.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProfileScheduler {
    @Autowired
    private ProfileService profileService;

    private ProfileScheduler() {
    }

    private static class Holder {
        private static final ProfileScheduler profileScheduler = new ProfileScheduler();
    }

    public static ProfileScheduler getInstance() {
        return Holder.profileScheduler;
    }

    @Scheduled(fixedDelay = 1000L * 60 * 15)
    public void runTokenCleaner() {
        log.info("eleonore - Running authentication token cleaner");
        this.profileService.cleanInvalidToken();
    }
}
