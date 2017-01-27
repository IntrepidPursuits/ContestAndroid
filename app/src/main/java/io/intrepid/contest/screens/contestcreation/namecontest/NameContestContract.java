package io.intrepid.contest.screens.contestcreation.namecontest;

import io.intrepid.contest.base.BaseContract;

class NameContestContract {

    public interface View extends BaseContract.View {
        void onContestNameEntered(String contestName);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        void updateContestName(String contestName);
    }
}
