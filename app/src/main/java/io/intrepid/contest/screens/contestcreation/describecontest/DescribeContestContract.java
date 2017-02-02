package io.intrepid.contest.screens.contestcreation.describecontest;

import io.intrepid.contest.base.BaseContract;

class DescribeContestContract {

    public interface View extends BaseContract.View {
        void saveContestDescription(String description);

        void setNextEnabled(boolean enabled);
    }

    public interface Presenter<View> {
        void onNextClicked(String description);

        void onNextInvalidated();

        void onNextValidated();

        void onTextChanged(CharSequence newDescription);
    }
}
