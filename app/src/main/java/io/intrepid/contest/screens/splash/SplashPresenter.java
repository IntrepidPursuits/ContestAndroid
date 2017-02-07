package io.intrepid.contest.screens.splash;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

public class SplashPresenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter {

    public static final String TEST_AUTHENTICATION_TOKEN = "e65114db-211e-4de9-8791-f6d6d17a8136";

    public SplashPresenter(@NonNull SplashContract.View view,
                           @NonNull PresenterConfiguration configuration) {
        super(view, configuration);

        // todo: set the authentication token when the app is downloaded, not here
        persistentSettings.setAuthenticationToken(TEST_AUTHENTICATION_TOKEN);
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
