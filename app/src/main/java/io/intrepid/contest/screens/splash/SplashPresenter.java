package io.intrepid.contest.screens.splash;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

public class SplashPresenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter {

    public SplashPresenter(@NonNull SplashContract.View view,
                           @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onCreateContestClicked() {
        view.showCreateContestScreen();
    }

    @Override
    public void onJoinContestClicked() {
        view.showJoinContestScreen();
    }
}
