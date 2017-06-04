package io.intrepid.contest.screens.contestcreation.namecontest;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contest;

public class NameContestPresenter extends BasePresenter<NameContestContract.View> implements NameContestContract.Presenter {

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
        getView().setNextEnabled(false);
    }

    @Override
    public void onContestTitleUpdated(String contestName) {
        contestBuilder.setTitle(contestName);
        getView().showNextScreen();
    }

    @Override
    public void onNextInvalidated() {
        getView().setNextEnabled(false);
    }

    @Override
    public void onNextValidated() {
        if (getView() != null) {
            getView().setNextEnabled(true);
        }
    }

    @Override
    public void onTextChanged(CharSequence newName) {
        if (newName.toString().isEmpty()) {
            getView().setNextEnabled(false);
        } else {
            getView().setNextEnabled(true);
        }
    }
}
