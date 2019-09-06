package com.dashboard.eleonore.profile;

import com.dashboard.eleonore.profile.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ProfileScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileScheduler.class);

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

    @Scheduled(fixedDelay = 1000l * 60 * 15)
    public void runTokenCleaner() {
        LOGGER.info("eleonore - Running authentication token cleaner");
        this.profileService.cleanInvalidToken();
    }
}
