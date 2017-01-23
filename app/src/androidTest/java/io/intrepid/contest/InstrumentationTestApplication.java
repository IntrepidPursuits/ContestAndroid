package io.intrepid.contest;

import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.rest.RestApi;
import io.intrepid.contest.rest.RetrofitClient;

public class InstrumentationTestApplication extends ContestApplication {
    private static RestApi restApiOverride = null;

    @Override
    public PresenterConfiguration getPresenterConfiguration() {
        return new PresenterConfiguration(restApiOverride != null ? restApiOverride : RetrofitClient.getApi());
    }

    public static void overrideRestApi(RestApi restApi) {
        restApiOverride = restApi;
    }

    public static void clearRestApiOverride() {
        restApiOverride = null;
    }
}