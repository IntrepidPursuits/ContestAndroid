package io.intrepid.contest.screens.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.screens.contestcreation.NewContestActivity;
import io.intrepid.contest.screens.conteststatus.ContestStatusActivity;
import io.intrepid.contest.screens.join.JoinActivity;
import timber.log.Timber;

import static android.view.View.VISIBLE;

public class SplashActivity extends BaseMvpActivity<SplashContract.Presenter> implements SplashContract.View {
    @BindView(R.id.splash_screen_actions_layout)
    RelativeLayout splashScreenActionsLayout;
    @BindView(R.id.ongoing_contests_recycler_view)
    RecyclerView activeContestsRecyclerView;
    private ContestAdapter contestAdapter;

    public static Intent makeIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }

    @NonNull
    @Override
    public SplashContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return new SplashPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        contestAdapter = new ContestAdapter(presenter);
        activeContestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        activeContestsRecyclerView.setAdapter(contestAdapter);
    }

    @OnClick(R.id.create_contest_button)
    public void onCreateButtonClicked() {
        presenter.onCreateContestClicked();
    }

    @OnClick(R.id.join_contest_button)
    public void onJoinContestClicked() {
        presenter.onJoinContestClicked();
    }

    @Override
    public void showCreateContestScreen() {
        Intent intent = NewContestActivity.createIntent(this);
        startActivity(intent);
    }

    @Override
    public void showJoinContestScreen() {
        startActivity(JoinActivity.makeIntent(this));
    }

    @Override
    public void showUserButtons() {
        splashScreenActionsLayout.setVisibility(VISIBLE);
    }

    @Override
    public void showOngoingContests(List<Contest> contests) {
        Timber.d("Contests size " + contests.size());
        contestAdapter.setData(contests);
    }

    @Override
    public void resumeContest(Contest contest) {
        startActivity(ContestStatusActivity.makeIntent(this));
    }
}
