package io.intrepid.contest.screens.splash;

import io.intrepid.contest.base.BaseContract;

interface SplashContract {

    interface View extends BaseContract.View {
        void showCreateContestScreen();

        void showJoinContestScreen();

        void showUserButtons();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onCreateContestClicked();

        void onJoinContestClicked();
    }
}
