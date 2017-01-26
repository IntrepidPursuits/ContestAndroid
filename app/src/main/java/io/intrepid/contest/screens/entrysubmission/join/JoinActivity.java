package io.intrepid.contest.screens.entrysubmission.join;

import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.screens.entrysubmission.join.JoinContract.Presenter;

public class JoinActivity extends BaseMvpActivity<Presenter> implements JoinContract.View {

    @NonNull
    @Override
    public Presenter createPresenter(PresenterConfiguration configuration) {
        return new JoinPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_join;
    }
}
