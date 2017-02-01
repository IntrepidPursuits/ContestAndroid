package io.intrepid.contest.testutils;

import android.support.annotation.NonNull;

import org.mockito.Mockito;

import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.rest.RestApi;
import io.intrepid.contest.settings.PersistentSettings;
import io.reactivex.schedulers.TestScheduler;

@SuppressWarnings("WeakerAccess")
public class TestPresenterConfiguration extends PresenterConfiguration {
    public TestPresenterConfiguration(@NonNull PersistentSettings persistentSettings,
                                      @NonNull RestApi restApi) {
        super(new TestScheduler(), new TestScheduler(), persistentSettings, restApi);
    }

    public static TestPresenterConfiguration createTestConfiguration() {
        RestApi mockApi = Mockito.mock(RestApi.class);
        PersistentSettings mockSettings = Mockito.mock(PersistentSettings.class);
        return new TestPresenterConfiguration(
                mockSettings,
                mockApi
        );
    }

    @NonNull
    @Override
    public TestScheduler getIoScheduler() {
        return (TestScheduler) super.getIoScheduler();
    }

    @NonNull
    @Override
    public TestScheduler getUiScheduler() {
        return (TestScheduler) super.getUiScheduler();
    }

    /**
     * Helper method for triggering pending Rx events
     */
    public void triggerRxSchedulers() {
        getIoScheduler().triggerActions();
        getUiScheduler().triggerActions();
    }
}

