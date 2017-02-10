package io.intrepid.contest.screens.contestoverview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;

public class ContestOverviewActivity extends BaseMvpActivity<ContestOverviewContract.Presenter>
        implements ContestOverviewContract.View {

    @BindView(R.id.contest_overview_intro_text_view)
    TextView introTextView;

    public static Intent makeIntent(Context context) {
        return new Intent(context, ContestOverviewActivity.class);
    }

    @NonNull
    @Override
    public ContestOverviewContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return new ContestOverviewPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_contest_overview;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        setActionBarDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void showContestName(String contestName) {
        setActionBarTitle(getResources().getString(R.string.status_waiting_submissions_judge_bar_title, contestName));
    }

    @Override
    public void showNumSubmissionsWaiting(int numSubmissionsWaiting) {
        String submissions = getResources()
                .getQuantityString(R.plurals.numberOfSubmissions, numSubmissionsWaiting, numSubmissionsWaiting);
        introTextView.setText(
                getResources().getString(R.string.contest_overview_intro, submissions));
    }
}
