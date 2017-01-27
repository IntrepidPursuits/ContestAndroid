package io.intrepid.contest.screens.entrysubmission.entryname;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

import static io.intrepid.contest.screens.entrysubmission.entryname.EntryNameContract.Presenter;
import static io.intrepid.contest.screens.entrysubmission.entryname.EntryNameContract.View;

class EntryNamePresenter extends BasePresenter<View> implements Presenter {

    EntryNamePresenter(@NonNull View view,
                       @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        view.showWelcomeMessage();
    }

    @Override
    public void onEntryNameFocusChanged(boolean isFocused) {
        if (isFocused) {
            view.showEntryNameButton();
        } else {
            view.hideEntryNameButton();
        }
    }

    @Override
    public void onEntryNameTextChanged(String newText) {
        if (newText.isEmpty()) {
            view.disableEntryNameButton();
        } else {
            view.enableEntryNameButton();
        }
    }

    @Override
    public void onEntryNameSubmitted(String entryName) {
        view.showEntryImageScreen(entryName);
    }
}
