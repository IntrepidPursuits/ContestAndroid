package io.intrepid.contest.base;

import android.support.annotation.NonNull;

import io.intrepid.contest.rest.RestApi;
import io.intrepid.contest.settings.PersistentSettings;
import io.reactivex.Scheduler;

/**
 * Wrapper class for common dependencies that all presenters are expected to have
 */
public class PresenterConfiguration {
    @NonNull
    private final Scheduler ioScheduler;
    @NonNull
    private final Scheduler uiScheduler;
    @NonNull
    private final PersistentSettings persistentSettings;
    @NonNull
    private final RestApi restApi;

    public PresenterConfiguration(@NonNull Scheduler ioScheduler,
                                  @NonNull Scheduler uiScheduler,
                                  @NonNull PersistentSettings persistentSettings,
                                  @NonNull RestApi restApi) {
        this.ioScheduler = ioScheduler;
        this.uiScheduler = uiScheduler;
        this.persistentSettings = persistentSettings;
        this.restApi = restApi;
    }

    @NonNull
    public Scheduler getIoScheduler() {
        return ioScheduler;
    }

    @NonNull
    public Scheduler getUiScheduler() {
        return uiScheduler;
    }

    @NonNull
    public PersistentSettings getPersistentSettings() {
        return persistentSettings;
    }

    @NonNull
    public RestApi getRestApi() {
        return restApi;
    }
}
