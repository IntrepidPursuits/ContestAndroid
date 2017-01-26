package io.intrepid.contest.screens.entrysubmission.join;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

import static io.intrepid.contest.screens.entrysubmission.join.JoinContract.View;

public class JoinPresenter extends BasePresenter<View> implements JoinContract.Presenter {

    public JoinPresenter(@NonNull View view, @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }
}
