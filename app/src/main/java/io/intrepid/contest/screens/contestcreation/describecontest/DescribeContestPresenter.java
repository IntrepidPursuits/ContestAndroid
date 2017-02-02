package io.intrepid.contest.screens.contestcreation.describecontest;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contest;

class DescribeContestPresenter extends BasePresenter<DescribeContestContract.View> implements DescribeContestContract.Presenter {

    DescribeContestPresenter(@NonNull DescribeContestContract.View view,
                             @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        view.setNextEnabled(false);
    }

    @Override
    public void onNextClicked(String description) {
        view.saveContestDescription(description);
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
