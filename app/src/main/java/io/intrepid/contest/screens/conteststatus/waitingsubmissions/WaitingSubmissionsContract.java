package io.intrepid.contest.screens.conteststatus.waitingsubmissions;

import io.intrepid.contest.base.BaseContract;

class WaitingSubmissionsContract {
    interface View extends BaseContract.View {
        void showNumSubmissionsMissing(int numSubmissionsMissing);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onNumSubmissionsMissingUpdated(int numSubmissionsMissing);
    }
}
