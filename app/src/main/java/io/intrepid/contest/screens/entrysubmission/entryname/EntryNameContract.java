package io.intrepid.contest.screens.entrysubmission.entryname;

import io.intrepid.contest.base.BaseContract;

class EntryNameContract {
    interface View extends BaseContract.View {
        void showEntryNameButton();

        void hideEntryNameButton();

        void showEntryImageScreen(String entryName);

        void showEntryNameIcon();

        void hideEntryNameIcon();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onEntryNameTextChanged(String newText);

        void onEntryNameSubmitted(String entryName);
    }
}
