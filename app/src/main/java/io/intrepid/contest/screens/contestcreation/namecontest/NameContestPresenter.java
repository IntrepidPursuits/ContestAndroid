package io.intrepid.contest.screens.contestcreation.namecontest;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.models.ContestProvider;

class NameContestPresenter extends BasePresenter<NameContestContract.View> implements NameContestContract.Presenter {

    NameContestPresenter(@NonNull NameContestContract.View view,
                         @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
    }

    @Override
    public void updateContestName(String contestName) {
        Contest.Builder contest = ContestProvider.getInstance().getLastEditedContest();
        contest.title = contestName;
        ContestProvider.getInstance().updateTemporaryContest(contest);
    }
}
