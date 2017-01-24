package io.intrepid.contest.splash;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

public class SplashPresenter extends BasePresenter implements SplashContract.Presenter {

    public SplashPresenter(@NonNull BaseContract.View view, @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }
}
