package io.intrepid.contest.screens.splash;

import io.intrepid.contest.base.BaseContract;

public class SplashContract {

    interface View extends BaseContract.View {
        void showCreateContestScreen();
        void showJoinContestScreen();
        void intializeViews();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onCreateContestClicked();
        void onJoinContestClicked();
    }
}
