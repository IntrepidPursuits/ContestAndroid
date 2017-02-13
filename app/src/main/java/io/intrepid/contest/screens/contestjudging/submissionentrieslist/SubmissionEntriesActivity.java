package io.intrepid.contest.screens.contestjudging.submissionentrieslist;

import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;

public class SubmissionEntriesActivity extends BaseMvpActivity<SubmissionEntriesPresenter> implements SubmissionEntriesContract.View {

    @NonNull
    @Override
    public SubmissionEntriesPresenter createPresenter(PresenterConfiguration configuration) {
        return new SubmissionEntriesPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.include_layout_recycler_view;
    }
}
