package io.intrepid.contest.screens.join;

import io.intrepid.contest.base.BaseContract;

class JoinContract {
    interface View extends BaseContract.View {
        void showSubmitButton();

        void hideSubmitButton();

        void showEntryNameScreen();

        void showInvalidCodeErrorMessage();

        void showContestStatusScreen();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onEntryCodeTextChanged(String newCode);

        void onSubmitButtonClicked(String code);
    }
}
