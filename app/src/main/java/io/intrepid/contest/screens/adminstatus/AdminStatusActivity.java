package io.intrepid.contest.screens.adminstatus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.customviews.ProgressIndicatorTextView;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.screens.splash.SplashActivity;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static io.intrepid.contest.screens.adminstatus.ConfirmStartContestDialog.AdminActionType.END_CONTEST;
import static io.intrepid.contest.screens.adminstatus.ConfirmStartContestDialog.AdminActionType.START_CONTEST;

public class AdminStatusActivity extends BaseMvpActivity<AdminStatusPresenter> implements AdminStatusContract.View {
    @BindView(R.id.entry_review_recycler_view)
    RecyclerView entriesRecyclerView;
    @BindView(R.id.awaiting_submission_text_indicator)
    ProgressIndicatorTextView awaitingSubmissionsIndicator;
    @BindView(R.id.judging_text_indicator)
    ProgressIndicatorTextView judgingIndicator;
    @BindView(R.id.end_of_contest_text_indicator)
    ProgressIndicatorTextView endOfContestIndicator;
    @BindView(R.id.bottom_navigation_button)
    Button bottomNavigationButton;

    private EntryReviewAdapter adapter = new EntryReviewAdapter();

    public static Intent makeIntent(Context context) {
        return new Intent(context, AdminStatusActivity.class);
    }

    @NonNull
    @Override
    public AdminStatusPresenter createPresenter(PresenterConfiguration configuration) {
        return new AdminStatusPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.admin_status_activity;
    }

    @Override
    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        setActionBarTitle(R.string.contest_status_bar_title);
        setActionBarDisplayHomeAsUpEnabled(true);
        entriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        entriesRecyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.bottom_navigation_button)
    public void onBottomNavigationButtonClicked() {
        presenter.onBottomNavigationButtonClicked();
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }

    @Override
    public void exitStatusScreen() {
        startActivity(SplashActivity.makeIntent(this));
    }

    private void updateStatusIndicator(ProgressIndicatorTextView textView, boolean inProgress) {
        if (inProgress) {
            textView.enableProgressAnimation();
        } else {
            textView.showCompleteIndicator();
        }
    }

    @Override
    public void showAwaitSubmissionsIndicator(boolean awaitingSubmissions) {
        updateStatusIndicator(awaitingSubmissionsIndicator, awaitingSubmissions);
    }

    @Override
    public void showJudgingStatusIndicator(boolean inProgress) {
        updateStatusIndicator(judgingIndicator, inProgress);
    }

    @Override
    public void showEndOfContestIndicator(boolean contestEnded) {
        updateStatusIndicator(endOfContestIndicator, contestEnded);
        bottomNavigationButton.setVisibility(contestEnded ? INVISIBLE : VISIBLE);
    }

    @Override
    public void showSubmittedEntries(List<Entry> entries) {
        adapter.setData(entries);
    }

    @Override
    public void showConfirmStartContestDialog() {
        ConfirmStartContestDialog.newInstance(START_CONTEST, presenter).show(getSupportFragmentManager());
    }

    @Override
    public void showConfirmEndContestDialog() {
        ConfirmStartContestDialog.newInstance(END_CONTEST, presenter).show(getSupportFragmentManager());
    }

    @Override
    public void advanceToJudgingIndicator() {
        showAwaitSubmissionsIndicator(false);
        showJudgingStatusIndicator(true);
        bottomNavigationButton.setText(R.string.common_end_contest);
    }
}
