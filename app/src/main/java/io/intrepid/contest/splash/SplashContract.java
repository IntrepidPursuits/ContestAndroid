package io.intrepid.contest.splash;

import io.intrepid.contest.base.BaseContract;

public interface SplashContract {

    interface View extends BaseContract.View {
        void showCreateContestScreen();

        void showJoinContestScreen();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onCreateContestClicked();

        void onJoinContestClicked();
    }
}
