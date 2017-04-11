package io.intrepid.contest.screens.conteststatus;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.rest.ContestWrapper;
import io.reactivex.functions.Consumer;

class ContestStatusContract {
    interface View extends BaseContract.View {
        void returnToSplashScreen();

        void showStatusWaitingFragment();

        void showResultsAvailableFragment();

        void showContestOverviewPage();

        void showAdminStatusPage();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onBackPressed();

        void requestContestDetails(Consumer<ContestWrapper> responseCallback, Consumer<Throwable> throwableCallback);
    }
}
