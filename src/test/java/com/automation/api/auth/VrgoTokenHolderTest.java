package com.automation.api.auth;

import com.automation.api.config.EnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class VrgoTokenHolderTest {

    @Test
    public void missingRefreshTokenAttemptsBrowserRecoveryBeforeFailing() {
        EnvironmentConfig config = EnvironmentConfig.load();
        AtomicBoolean recoveryAttempted = new AtomicBoolean();
        VrgoTokenHolder holder = new VrgoTokenHolder(
                config,
                ignored -> {
                    recoveryAttempted.set(true);
                    return null;
                },
                false
        );

        IllegalStateException error = Assert.expectThrows(
                IllegalStateException.class,
                holder::ensureValidAccessToken
        );

        Assert.assertTrue(recoveryAttempted.get(), "Browser recovery should be attempted");
        Assert.assertTrue(error.getMessage().contains("VRGO_AUTH_USERNAME_TEST"));
    }
}
