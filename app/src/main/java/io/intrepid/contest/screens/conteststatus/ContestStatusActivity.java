package io.intrepid.contest.screens.conteststatus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.rest.ContestResponse;
import io.intrepid.contest.screens.conteststatus.resultsavailable.ResultsAvailableFragment;
import io.intrepid.contest.screens.conteststatus.waitingsubmissions.WaitingSubmissionsFragment;
import io.intrepid.contest.screens.splash.SplashActivity;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

import static android.view.View.GONE;

public class ContestStatusActivity extends BaseMvpActivity<ContestStatusContract.Presenter>
        implements ContestStatusContract.View, ContestStatusActivityContract {
    @BindView(R.id.temporary_skip_button)
    Button temporarySkipButton;

    public static Intent makeIntent(Context context) {
        return new Intent(context, ContestStatusActivity.class);
    }

    @NonNull
    @Override
    public ContestStatusContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return new ContestStatusPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_fragment_container;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        setActionBarTitle(R.string.contest_status_bar_title);
        setActionBarDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        startActivity(SplashActivity.makeIntent(this));
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void showWaitingSubmissionsFragment(int numSubmissionsMissing) {
        WaitingSubmissionsFragment fragment = (WaitingSubmissionsFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_waiting_submissions);

        if (fragment != null) {
            Timber.d("Update existing WaitingSubmissionsFragment");
            fragment.updateNumSubmissionsMissing(numSubmissionsMissing);
        } else {
            Timber.d("Create WaitingSubmissionsFragment");
            fragment = new WaitingSubmissionsFragment();
            Bundle args = new Bundle();
            args.putInt(WaitingSubmissionsFragment.NUM_SUBMISSIONS_MISSING, numSubmissionsMissing);
            fragment.setArguments(args);
        }

        replaceFragment(fragment);
    }

    @Override
    public void showResultsAvailableFragment() {
        ResultsAvailableFragment fragment = (ResultsAvailableFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_results_available);

        if (fragment == null) {
            Timber.d("Create WaitingSubmissionsFragment");
            fragment = new ResultsAvailableFragment();
        }

        replaceFragment(fragment);
    }

    @OnClick(R.id.temporary_skip_button)
    public void onTemporarySkipButtonClicked() {
        // TODO: once API endpoing works, remove this button and actions triggered by it
        presenter.onTemporarySkipButtonClicked();
        temporarySkipButton.setVisibility(GONE);
    }

    @Override
    public void requestContestDetails(Consumer<ContestResponse> responseCallback,
                                      Consumer<Throwable> throwableCallback) {
        presenter.requestContestDetails(responseCallback, throwableCallback);
    }
}
