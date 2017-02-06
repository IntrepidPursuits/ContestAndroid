package io.intrepid.contest.screens.conteststatus.waitingsubmissions;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

class WaitingSubmissionsPresenter extends BasePresenter<WaitingSubmissionsContract.View>
        implements WaitingSubmissionsContract.Presenter {

    WaitingSubmissionsPresenter(@NonNull WaitingSubmissionsContract.View view,
                                @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onNumSubmissionsMissingUpdated(int numSubmissionsMissing) {
        view.showNumSubmissionsMissing(numSubmissionsMissing);
    }
}
