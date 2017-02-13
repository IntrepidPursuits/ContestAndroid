package io.intrepid.contest.screens.contestoverview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.ScoreWeight;

public class ContestOverviewActivity extends BaseMvpActivity<ContestOverviewContract.Presenter>
        implements ContestOverviewContract.View {
    @BindView(R.id.contest_overview_intro_text_view)
    TextView introTextView;
    @BindView(R.id.contest_overview_description_text_view)
    TextView descriptionTextView;
    @BindView(R.id.generic_recycler_view)
    RecyclerView categoriesRecyclerView;
    private DualCategoryScoreAdapter categoryAdapter;

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
        setupCategoriesRecyclerView();
        setupScoreWeightsRecyclerView();
    }

    @OnClick(R.id.contest_overview_submit_button)
    public void onOverviewSubmitButtonClicked() {
        presenter.onOverViewSubmitButtonClicked();
    }

    private void setupCategoriesRecyclerView() {
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new DualCategoryScoreAdapter();
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public void showContestName(String contestName) {
        setActionBarTitle(getResources().getString(R.string.status_waiting_submissions_judge_bar_title, contestName));
    }

    @Override
    public void showContestDescription(String description) {
        descriptionTextView.setText(description);
    }

    @Override
    public void showCategoriesAndWeights(List<Category> categories, List<ScoreWeight> weights) {
        categoryAdapter.setData(categories, weights);
    }

    @Override
    public void showNumSubmissionsWaiting(int numSubmissionsWaiting) {
        String submissions = getResources()
                .getQuantityString(R.plurals.numberOfSubmissions, numSubmissionsWaiting, numSubmissionsWaiting);
        introTextView.setText(
                getResources().getString(R.string.contest_overview_intro, submissions));
    }

    @Override
    public void advanceToJudgingScreen(Contest contest) {
        startActivity(SubmissionEntriesActivity.makeIntent(this, contest.getEntries()));
    }
}
