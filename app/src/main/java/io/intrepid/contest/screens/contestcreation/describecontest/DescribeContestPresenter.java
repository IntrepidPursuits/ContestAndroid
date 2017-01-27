package io.intrepid.contest.screens.contestcreation.describecontest;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.ContestProvider;

class DescribeContestPresenter extends BasePresenter<DescribeContestContract.View> implements DescribeContestContract.Presenter {

    DescribeContestPresenter(@NonNull DescribeContestContract.View view,
                             @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void submitContestDescription(String descriptionName) {
        ContestProvider.getInstance().editDescription(descriptionName);
    }
}
