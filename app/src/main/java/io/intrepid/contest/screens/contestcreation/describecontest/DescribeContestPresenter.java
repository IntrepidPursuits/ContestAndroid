package io.intrepid.contest.screens.contestcreation.describecontest;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contest;

class DescribeContestPresenter extends BasePresenter<DescribeContestContract.View> implements DescribeContestContract.Presenter {

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
        view.setNextEnabled(false);
    }

    @Override
    public void onNextClicked(String description) {
        contestBuilder.setDescription(description);
        view.showNextScreen();
    }

    @Override
    public void onNextInvalidated() {
        view.setNextEnabled(false);
    }

    @Override
    public void onNextValidated() {
        view.setNextEnabled(true);
    }

    @Override
    public void onTextChanged(CharSequence newDescription) {
        if (TextUtils.isEmpty(newDescription)) {
            view.setNextEnabled(false);
        } else {
            view.setNextEnabled(true);
        }
    }
}
