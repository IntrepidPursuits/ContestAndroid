package io.intrepid.contest.screens.contestcreation.invitecontestants;

import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseViewPagerFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.base.TextValidatableView;

public class InviteContestantsFragment extends BaseViewPagerFragment<InviteContestantsPresenter> implements InviteContestantsContract.View, TextValidatableView {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragments_invite_contestants;
    }

    @NonNull
    @Override
    public InviteContestantsPresenter createPresenter(PresenterConfiguration configuration) {
        return new InviteContestantsPresenter(this, configuration);
    }

    @Override
    public boolean areAllFieldValid() {
        return true;
    }

    @Override
    public int errorMessage() {
        return 0;
    }

    @Override
    public void submitText() {

    }
}

