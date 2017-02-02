package io.intrepid.contest.screens.conteststatus;

import io.intrepid.contest.base.BaseContract;

class ContestStatusContract {
    interface View extends BaseContract.View {
        void showWaitingSubmissionsFragment(int numSubmissionsMissing);
    }

    interface Presenter extends BaseContract.Presenter<View> {
    }
}
