package io.intrepid.contest.screens.splash;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

public class SplashPresenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter {

    public static final String TEST_AUTHENTICATION_TOKEN = "503adec2-6b60-422d-9f94-3095af9c1416";

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
