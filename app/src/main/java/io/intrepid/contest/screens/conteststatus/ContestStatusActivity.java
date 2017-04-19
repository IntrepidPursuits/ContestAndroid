package io.intrepid.contest.screens.conteststatus;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.rest.ContestWrapper;
import io.intrepid.contest.screens.adminstatus.AdminStatusActivity;
import io.intrepid.contest.screens.contestoverview.ContestOverviewActivity;
import io.intrepid.contest.screens.conteststatus.resultsavailable.ResultsAvailableFragment;
import io.intrepid.contest.screens.conteststatus.statuswaiting.StatusWaitingFragment;
import io.intrepid.contest.screens.splash.SplashActivity;
import io.reactivex.functions.Consumer;

public class ContestStatusActivity extends BaseMvpActivity<ContestStatusContract.Presenter, ContestStatusContract.View>
        implements ContestStatusContract.View, ContestStatusActivityContract {

    private static final String JUDGE_JUST_SUBMITTED_KEY = "judge_submitted_key";

    public static Intent makeIntent(Context context, boolean judgeSubmitted) {
        return makeIntent(context).putExtra(JUDGE_JUST_SUBMITTED_KEY, judgeSubmitted);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ContestStatusActivity.class);
    }

    @NonNull
    @Override
    public ContestStatusContract.Presenter createPresenter(PresenterConfiguration configuration) {
        boolean recentlySubmittedAsJudge = getIntent().getBooleanExtra(JUDGE_JUST_SUBMITTED_KEY, false);
        return new ContestStatusPresenter(this, configuration, recentlySubmittedAsJudge);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_contest_status;
    }

    @Override
    public void onBackPressed() {
        getPresenter().onBackPressed();
    }

    @Override
    public void returnToSplashScreen() {
        startActivity(SplashActivity.makeIntent(this));
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void showStatusWaitingFragment() {
        StatusWaitingFragment fragment = (StatusWaitingFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_status_waiting);

        if (fragment == null) {
            fragment = new StatusWaitingFragment();
        }

        replaceFragment(fragment);
    }

    @Override
    public void showResultsAvailableFragment() {
        ResultsAvailableFragment fragment = (ResultsAvailableFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_results_available);

        if (fragment == null) {
            fragment = new ResultsAvailableFragment();
        }

        replaceFragment(fragment);
    }

    @Override
    public void requestContestDetails(Consumer<ContestWrapper> responseCallback,
                                      Consumer<Throwable> throwableCallback) {
        getPresenter().requestContestDetails(responseCallback, throwableCallback);
    }

    @Override
    public void showContestOverviewPage() {
        startActivity(ContestOverviewActivity.makeIntent(this));
    }

    @Override
    public void showAdminStatusPage() {
        startActivity(AdminStatusActivity.makeIntent(this));
    }
}
