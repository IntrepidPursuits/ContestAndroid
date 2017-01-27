package io.intrepid.contest.screens.contestcreation.reviewcontest;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

public class ReviewContestPresenter extends BasePresenter<ReviewContestContract.View> implements ReviewContestContract.Presenter {

    public ReviewContestPresenter(@NonNull ReviewContestContract.View view,
                                  @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }
}
