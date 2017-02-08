package io.intrepid.contest.screens.conteststatus;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.rest.ContestResponse;
import io.reactivex.functions.Consumer;

class ContestStatusContract {
    interface View extends BaseContract.View {
        void showWaitingSubmissionsFragment(int numSubmissionsMissing);

        void showResultsAvailableFragment();

        void showContestOverviewPage();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onTemporarySkipButtonClicked();

        void requestContestDetails(Consumer<ContestResponse> responseCallback, Consumer<Throwable> throwableCallback);
    }
}
