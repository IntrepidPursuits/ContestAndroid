package io.intrepid.contest.screens.contestcreation.namecontest;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

public class NameContestPresenter extends BasePresenter<NameContestContract.View> implements NameContestContract.Presenter {

    public NameContestPresenter(@NonNull NameContestContract.View view,
                                @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }
}
