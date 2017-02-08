package io.intrepid.contest.screens.conteststatus.resultsavailable;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.rest.ContestWrapper;
import io.reactivex.functions.Consumer;

class ResultsAvailableContract {

    interface View extends BaseContract.View {
        void showContestName(String contestName);

        void showResultsPage();

        void requestContestDetails(Consumer<ContestWrapper> responseCallback, Consumer<Throwable> throwableCallback);
    }

    interface Presenter extends BaseContract.Presenter<ResultsAvailableContract.View> {
        void onViewResultsButtonClicked();
    }
}

