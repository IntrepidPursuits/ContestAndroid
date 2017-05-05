package io.intrepid.contest.screens.contestcreation.namecontest;

import io.intrepid.contest.base.BaseContract;

class NameContestContract {

    public interface View extends BaseContract.View {
        void onNextPageEnabledChanged(boolean enabled);

        void showNextScreen();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        void onContestTitleUpdated(String contestName);

        void onNextInvalidated();

        void onNextValidated();

        void onTextChanged(CharSequence newName);

        boolean isNextPageButtonEnabled();
    }
}
