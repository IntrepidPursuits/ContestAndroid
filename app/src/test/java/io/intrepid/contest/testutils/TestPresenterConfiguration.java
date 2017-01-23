package io.intrepid.contest.testutils;

import android.support.annotation.NonNull;

import org.mockito.Mockito;

import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.rest.RestApi;

@SuppressWarnings("WeakerAccess")
public class TestPresenterConfiguration extends PresenterConfiguration {
    public static TestPresenterConfiguration createTestConfiguration() {
        RestApi mockApi = Mockito.mock(RestApi.class);
        return new TestPresenterConfiguration(mockApi);
    }

    public TestPresenterConfiguration(@NonNull RestApi restApi) {
        super(restApi);
    }
}
