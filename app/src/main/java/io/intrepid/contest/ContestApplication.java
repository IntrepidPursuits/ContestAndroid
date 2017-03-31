package io.intrepid.contest;

import android.app.Application;
import android.os.StrictMode;

import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.rest.RestApi;
import io.intrepid.contest.rest.RetrofitClient;
import io.intrepid.contest.settings.PersistentSettings;
import io.intrepid.contest.settings.SharedPreferencesManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ContestApplication extends Application {

    private PersistentSettings settings;

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build()); //todo - remove strictmode
        Timber.plant(new Timber.DebugTree());
        settings = SharedPreferencesManager.getInstance(this);
        RetrofitClient.init(settings);
        initCalligraphy();
    }

    public void resetState() {
        settings.clear();
        RetrofitClient.init(settings);
    }

    private void initCalligraphy() {
        CalligraphyConfig config = new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.AvenirNext_Regular))
                .setFontAttrId(R.attr.fontPath)
                .build();
        CalligraphyConfig.initDefault(config);
    }

    public PresenterConfiguration getPresenterConfiguration() {
        RestApi api = (BuildConfig.DEV_BUILD) ? RetrofitClient.getMockApi() : RetrofitClient.getApi();

        return new PresenterConfiguration(
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                SharedPreferencesManager.getInstance(this),
                api
        );
    }
}
