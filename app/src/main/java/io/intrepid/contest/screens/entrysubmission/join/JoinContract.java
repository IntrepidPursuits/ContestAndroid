package io.intrepid.contest.screens.entrysubmission.join;

import io.intrepid.contest.base.BaseContract;

public class JoinContract {
    interface View extends BaseContract.View {
        void showSubmitButton();

        void hideSubmitButton();

        void showEntryNameScreen();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onEntryCodeTextChanged(String newCode);

        void onSubmitButtonClicked();
    }
}
