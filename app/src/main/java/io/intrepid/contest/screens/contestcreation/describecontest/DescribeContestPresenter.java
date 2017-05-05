package io.intrepid.contest.screens.contestcreation.describecontest;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contest;

public class DescribeContestPresenter extends BasePresenter<DescribeContestContract.View> implements DescribeContestContract.Presenter {

    private final Contest.Builder contestBuilder;

    DescribeContestPresenter(@NonNull DescribeContestContract.View view,
                             @NonNull PresenterConfiguration configuration,
                             Contest.Builder contestBuilder) {
        super(view, configuration);
        this.contestBuilder = contestBuilder;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getView().setNextEnabled(true);
    }

    @Override
    public void onNextPageButtonClicked(String description) {
        contestBuilder.setDescription(description);
        getView().showNextScreen();
    }
}
