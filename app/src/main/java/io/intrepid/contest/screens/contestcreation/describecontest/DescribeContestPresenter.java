package io.intrepid.contest.screens.contestcreation.describecontest;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

public class DescribeContestPresenter extends BasePresenter<DescribeContestContract.View> implements DescribeContestContract.Presenter{

    public DescribeContestPresenter(@NonNull DescribeContestContract.View view,
                                    @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void submitContestDescription(String descriptionName) {

    }
}
