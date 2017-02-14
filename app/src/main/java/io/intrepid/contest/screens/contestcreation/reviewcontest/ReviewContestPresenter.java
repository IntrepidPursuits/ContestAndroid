package io.intrepid.contest.screens.contestcreation.reviewcontest;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contest;

class ReviewContestPresenter extends BasePresenter<ReviewContestContract.View> implements ReviewContestContract.Presenter {

    private final Contest.Builder contestBuilder;

    ReviewContestPresenter(@NonNull ReviewContestContract.View view,
                           @NonNull PresenterConfiguration configuration,
                           @NonNull Contest.Builder contestBuilder) {
        super(view, configuration);
        this.contestBuilder = contestBuilder;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        view.showReviewPage(contestBuilder);
    }

    @Override
    public void onContestTitleSelected() {
        view.showEditTitlePage(contestBuilder);
    }

    @Override
    public void onContestDescriptionSelected() {
        view.showEditDescriptionPage(contestBuilder);
    }
}
