package io.intrepid.contest.screens.entrysubmission.join;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

import static io.intrepid.contest.screens.entrysubmission.join.JoinContract.View;

class JoinPresenter extends BasePresenter<View> implements JoinContract.Presenter {

    JoinPresenter(@NonNull View view, @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onEntryCodeTextChanged(String newCode) {
        if (newCode.isEmpty()) {
            view.disableSubmitButton();
        } else {
            view.enableSubmitButton();
        }
    }

    @Override
    public void onSubmitButtonClicked() {
        view.showEntryNameScreen();
    }
}
