package io.intrepid.contest;

import android.app.Application;

import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.rest.RetrofitClient;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ContestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initCalligraphy();
    }

    private void initCalligraphy() {
        CalligraphyConfig config = new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.Roboto_Regular))
                .setFontAttrId(R.attr.fontPath)
                .build();
        CalligraphyConfig.initDefault(config);
    }

    public PresenterConfiguration getPresenterConfiguration() {
        return new PresenterConfiguration(RetrofitClient.getApi());
    }
}
