package io.intrepid.contest.screens.contestoverview;

import io.intrepid.contest.base.BaseContract;

class ContestOverviewContract {
    interface View extends BaseContract.View {
        void showContestName(String contestName);

        void showNumSubmissionsWaiting(int numSubmissionsWaiting);
    }

    interface Presenter extends BaseContract.Presenter<View> {

    }
}
