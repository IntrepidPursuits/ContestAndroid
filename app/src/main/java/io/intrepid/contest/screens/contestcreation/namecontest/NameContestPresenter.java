package io.intrepid.contest.screens.contestcreation.namecontest;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contest;

class NameContestPresenter extends BasePresenter<NameContestContract.View> implements NameContestContract.Presenter {

    private final Contest.Builder contestBuilder;

    NameContestPresenter(@NonNull NameContestContract.View view,
                         @NonNull PresenterConfiguration configuration,
                         Contest.Builder contestBuilder) {
        super(view, configuration);
        this.contestBuilder = contestBuilder;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        view.setNextEnabled(false);
    }

    @Override
    public void onContestTitleUpdated(String contestName) {
        contestBuilder.setTitle(contestName);
        view.showNextScreen();
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
