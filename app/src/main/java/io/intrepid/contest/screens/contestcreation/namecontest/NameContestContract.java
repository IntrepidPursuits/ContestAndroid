package io.intrepid.contest.screens.contestcreation.namecontest;

import io.intrepid.contest.base.BaseContract;

class NameContestContract {

    public interface View extends BaseContract.View {
        void saveEnteredName(String contestName);

        void setNextEnabled(boolean enabled);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        void onContestNameUpdate(String contestName);

        void onNextInvalidated();

        void onNextValidated();

        void onTextChanged(CharSequence newName);
    }
}
