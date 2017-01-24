package io.intrepid.contest.splash;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;

public class SplashActivity extends BaseMvpActivity<SplashContract.Presenter> implements SplashContract.View {

    @NonNull
    @Override
    public SplashContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return null;
    }

    @Override
    protected int getLayoutResourceId() {
        return 0;
    }
}
