package io.intrepid.contest.screens.contestcreation.invitecontestants;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

class InviteContestantsPresenter extends BasePresenter<InviteContestantsContract.View> implements InviteContestantsContract.View {

    InviteContestantsPresenter(@NonNull InviteContestantsContract.View view,
                               @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }
}
