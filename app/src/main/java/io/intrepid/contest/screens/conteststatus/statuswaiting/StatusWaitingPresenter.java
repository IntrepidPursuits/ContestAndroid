package io.intrepid.contest.screens.conteststatus.statuswaiting;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

class StatusWaitingPresenter extends BasePresenter<StatusWaitingContract.View>
        implements StatusWaitingContract.Presenter {

    StatusWaitingPresenter(@NonNull StatusWaitingContract.View view,
                           @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }
}
