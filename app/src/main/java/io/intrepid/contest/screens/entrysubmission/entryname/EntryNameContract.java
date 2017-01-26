package io.intrepid.contest.screens.entrysubmission.entryname;

import io.intrepid.contest.base.BaseContract;

public class EntryNameContract {
    interface View extends BaseContract.View {
        void setContestName(String contestName);

        void showEntryNameButton();

        void hideEntryNameButton();

        void enableEntryNameButton();

        void disableEntryNameButton();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onEntryNameFocusChanged(boolean isFocused);

        void onEntryNameTextChanged(String newText);
    }
}
