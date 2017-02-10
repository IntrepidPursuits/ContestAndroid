package io.intrepid.contest.screens.conteststatus.waitingsubmissions;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.rest.ContestWrapper;
import io.reactivex.functions.Consumer;

class WaitingSubmissionsContract {
    interface View extends BaseContract.View {
        void showNumSubmissionsMissing(int numSubmissionsMissing);

        void showJudgeUI();

        void showContestName(String contestName);

        void requestContestDetails(Consumer<ContestWrapper> contestResponseConsumer,
                                   Consumer<Throwable> throwableConsumer);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onNumSubmissionsMissingUpdated(int numSubmissionsMissing);
    }
}
