package io.intrepid.contest.screens.contestcreation.describecontest;

import io.intrepid.contest.base.BaseContract;

class DescribeContestContract {

    public interface View extends BaseContract.View {
        void setNextEnabled(boolean enabled);

        void showNextScreen();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        void onNextPageButtonClicked(String description);

        boolean isNextPageButtonEnabled();
    }
}
