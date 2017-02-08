package io.intrepid.contest;

import android.app.Application;

import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.rest.RetrofitClient;
import io.intrepid.contest.settings.SharedPreferencesManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ContestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        RetrofitClient.init(SharedPreferencesManager.getInstance(this));
        initCalligraphy();
    }

    private void initCalligraphy() {
        CalligraphyConfig config = new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.AvenirNext_Regular))
                .setFontAttrId(R.attr.fontPath)
                .build();
        CalligraphyConfig.initDefault(config);
    }

    public PresenterConfiguration getPresenterConfiguration() {
        return new PresenterConfiguration(
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                SharedPreferencesManager.getInstance(this),
                RetrofitClient.getMockApi()
        );
    }
}
