package io.intrepid.contest;

import android.os.AsyncTask;

import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.rest.RestApi;
import io.intrepid.contest.rest.RetrofitClient;
import io.intrepid.contest.settings.SharedPreferencesManager;
import io.intrepid.contest.settings.PersistentSettings;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InstrumentationTestApplication extends ContestApplication {
    private static RestApi restApiOverride = null;
    private static PersistentSettings persistentSettingsOverride = null;

    public static void overrideRestApi(RestApi restApi) {
        restApiOverride = restApi;
    }

    public static void clearRestApiOverride() {
        restApiOverride = null;
    }

    public static void overrideUserSettings(PersistentSettings persistentSettings) {
        persistentSettingsOverride = persistentSettings;
    }

    public static void clearUserSettingsOverride() {
        persistentSettingsOverride = null;
    }

    @Override
    public PresenterConfiguration getPresenterConfiguration() {
        return new PresenterConfiguration(
                // using AsyncTask executor since Espresso automatically waits for it to clear before proceeding
                Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR),
                AndroidSchedulers.mainThread(),
                persistentSettingsOverride != null ? persistentSettingsOverride : SharedPreferencesManager.getInstance(this),
                restApiOverride != null ? restApiOverride : RetrofitClient.getApi()
        );
    }
}
