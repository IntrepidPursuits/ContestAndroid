package io.intrepid.contest.screens.entrysubmission.entryname;

import io.intrepid.contest.base.BaseContract;

class EntryNameContract {
    interface View extends BaseContract.View {
        void showWelcomeMessage();

        void enableEntryNameButton();

        void disableEntryNameButton();

        void showEntryImageScreen(String entryName);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onEntryNameTextChanged(String newText);

        void onEntryNameSubmitted(String entryName);
    }
}
