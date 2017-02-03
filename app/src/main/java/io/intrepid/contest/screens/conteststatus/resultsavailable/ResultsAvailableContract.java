package io.intrepid.contest.screens.conteststatus.resultsavailable;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.rest.ContestResponse;
import io.reactivex.functions.Consumer;

class ResultsAvailableContract {

    interface View extends BaseContract.View {
        void showContestName(String contestName);

        void showResultsPage();

        void requestContestDetails(Consumer<ContestResponse> responseCallback, Consumer<Throwable> throwableCallback);
    }

    interface Presenter extends BaseContract.Presenter<ResultsAvailableContract.View> {
        void onViewResultsButtonClicked();
    }
}

