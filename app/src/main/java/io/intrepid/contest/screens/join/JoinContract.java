package io.intrepid.contest.screens.join;

import io.intrepid.contest.base.BaseContract;

class JoinContract {
    interface View extends BaseContract.View {
        void showSubmitButton();

        void hideSubmitButton();

        void showEntryNameScreen();

        void showInvalidCodeErrorMessage();

        void showContestStatusScreen();

        String getLastCopiedText();

        void showClipboardData(String potentialCodeFromClipboard);

        void cancelJoinContest();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onEntryCodeTextChanged(String newCode);

        void onSubmitButtonClicked(String code);

        void onBackPressed();
    }
}
