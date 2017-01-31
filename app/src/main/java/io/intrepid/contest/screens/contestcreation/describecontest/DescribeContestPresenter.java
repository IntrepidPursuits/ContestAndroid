package io.intrepid.contest.screens.contestcreation.describecontest;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contest;

class DescribeContestPresenter extends BasePresenter<DescribeContestContract.View> implements DescribeContestContract.Presenter {

    DescribeContestPresenter(@NonNull DescribeContestContract.View view,
                             @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onNextClicked(String description) {
        if(description.isEmpty()){
            showError();
            return;
        }else{
            view.saveContestDescription(description);
        }
    }

    private void showError() {
        view.showError();
    }
}
