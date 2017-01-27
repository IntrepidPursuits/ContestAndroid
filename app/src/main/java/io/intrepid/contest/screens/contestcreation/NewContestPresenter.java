package io.intrepid.contest.screens.contestcreation;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.base.TextValidatableView;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.models.ContestProvider;
import timber.log.Timber;

class NewContestPresenter extends BasePresenter<NewContestContract.View> implements NewContestContract.Presenter {
    private int screenIndex = 0;

    public NewContestPresenter(@NonNull NewContestContract.View view,
                               @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        initializeContestData();
    }

    private void initializeContestData() {
        view.showContestSubmissionPage(0);
    }

    @Override
    public void onNextButtonClicked() {
        try {
            TextValidatableView view = this.view.getChildEditFragment(screenIndex);
            if (view.areAllFieldValid()) {
                view.submitText();
            }
            this.view.showContestSubmissionPage(++screenIndex);
        } catch (IndexOutOfBoundsException oboe) {
            view.onContestEditComplete();
        }
    }

    @Override
    public void onBackButtonClicked() {
        if (screenIndex == 0) {
            view.cancelEdit();
        } else {
            view.showContestSubmissionPage(--screenIndex);
        }
    }

    @Override
    public void saveContest() {
        Contest contestSubmission = ContestProvider.getInstance().getLastEditedContest().build();
        Timber.d(contestSubmission.toString());
        ContestProvider.getInstance().add(contestSubmission);
        view.showNewlyAddedConest(contestSubmission);
    }
}
