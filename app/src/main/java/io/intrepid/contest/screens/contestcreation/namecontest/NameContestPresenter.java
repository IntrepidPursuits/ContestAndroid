package io.intrepid.contest.screens.contestcreation.namecontest;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

class NameContestPresenter extends BasePresenter<NameContestContract.View> implements NameContestContract.Presenter {

    NameContestPresenter(@NonNull NameContestContract.View view,
                         @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        view.setNextEnabled(false);
    }

    @Override
    public void onContestNameUpdate(String contestName) {
        view.saveEnteredName(contestName);
    }

    @Override
    public void onNextInvalidated() {
        view.setNextEnabled(false);
    }

    @Override
    public void onNextValidated() {
        if (view != null) {
            view.setNextEnabled(true);
        }
    }

    @Override
    public void onTextChanged(CharSequence newName) {
        if (TextUtils.isEmpty(newName)) {
            view.setNextEnabled(false);
        } else {
            view.setNextEnabled(true);
        }
    }
}
