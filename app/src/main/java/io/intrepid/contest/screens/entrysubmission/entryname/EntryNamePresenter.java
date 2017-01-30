package io.intrepid.contest.screens.entrysubmission.entryname;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

class EntryNamePresenter extends BasePresenter<EntryNameContract.View> implements EntryNameContract.Presenter {

    EntryNamePresenter(@NonNull EntryNameContract.View view,
                       @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onEntryNameTextChanged(String newText) {
        if (newText.isEmpty()) {
            view.hideEntryNameButton();
            view.showEntryNameIcon();
        } else {
            view.showEntryNameButton();
            view.hideEntryNameIcon();
        }
    }

    @Override
    public void onEntryNameSubmitted(String entryName) {
        view.showEntryImageScreen(entryName);
    }
}
