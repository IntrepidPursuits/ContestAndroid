package io.intrepid.contest.screens.contestcreation.namecontest;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contest;

public class NameContestPresenter extends BasePresenter<NameContestContract.View> implements NameContestContract.Presenter {
    private final Contest.Builder contestBuilder;
    private boolean nextPageButtonEnabled = false;

    NameContestPresenter(@NonNull NameContestContract.View view,
                         @NonNull PresenterConfiguration configuration,
                         Contest.Builder contestBuilder) {
        super(view, configuration);
        this.contestBuilder = contestBuilder;
    }

    @Override
    public void onContestTitleUpdated(String contestName) {
        contestBuilder.setTitle(contestName);
        getView().showNextScreen();
    }

    @Override
    public void onNextInvalidated() {
        updateNextPageButtonEnabled(false);
    }

    @Override
    public void onNextValidated() {
        if (getView() != null) {
            updateNextPageButtonEnabled(true);
        }
    }

    @Override
    public void onTextChanged(CharSequence newName) {
        if (newName.toString().isEmpty()) {
            updateNextPageButtonEnabled(false);
        } else {
            updateNextPageButtonEnabled(true);
        }
    }

    @Override
    public boolean isNextPageButtonEnabled() {
        return nextPageButtonEnabled;
    }

    private void updateNextPageButtonEnabled(boolean isEnabled) {
        if (isEnabled != nextPageButtonEnabled) {
            nextPageButtonEnabled = isEnabled;
            getView().onNextPageEnabledChanged(isEnabled);
        }
    }
}
