package io.intrepid.contest.screens.contestcreation.invitecontestants;

import android.support.annotation.NonNull;

import io.intrepid.contest.BaseSlideFragment;
import io.intrepid.contest.R;
import io.intrepid.contest.base.PresenterConfiguration;

public class InviteContestantsFragment extends BaseSlideFragment<InviteContestantsPresenter> implements InviteContestantsContract.View {

    @Override
    public boolean canMoveFurther() {
        return false;
    }

    @Override
    public int cantMoveFurtherErrorMessage() {
        return 0;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragments_invite_contestants;
    }

    @NonNull
    @Override
    public InviteContestantsPresenter createPresenter(PresenterConfiguration configuration) {
        return new InviteContestantsPresenter();
    }

}

