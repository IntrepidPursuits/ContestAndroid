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
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.screens.contestcreation.categorieslist.CategoryAdapter;
import timber.log.Timber;

public class ContestOverviewActivity extends BaseMvpActivity<ContestOverviewContract.Presenter>
        implements ContestOverviewContract.View {

    @BindView(R.id.contest_overview_intro_text_view)
    TextView introTextView;
    @BindView(R.id.contest_overview_description_text_view)
    TextView descriptionTextView;
    @BindView(R.id.categories_recycler_view)
    RecyclerView categoriesRecyclerView;
    @BindView(R.id.score_weight_guide_recycler_view)
    RecyclerView scoreWeightRecyclerView;
    private CategoryAdapter categoryAdapter;
    private ScoreWeightAdapter scoreWeightAdapter;

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

    private void setupCategoriesRecyclerView() {
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryAdapter(this, R.layout.category_overview_row_item);
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    private void setupScoreWeightsRecyclerView() {
        scoreWeightRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        scoreWeightAdapter = new ScoreWeightAdapter();
        scoreWeightRecyclerView.setAdapter(scoreWeightAdapter);
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
    public void showCategories(List<Category> categories) {
        categoryAdapter.setCategories(categories);
    }

    @Override
    public void showNumSubmissionsWaiting(int numSubmissionsWaiting) {
        String submissions = getResources()
                .getQuantityString(R.plurals.numberOfSubmissions, numSubmissionsWaiting, numSubmissionsWaiting);
        introTextView.setText(
                getResources().getString(R.string.contest_overview_intro, submissions));
    }

    @Override
    public void showRatingGuide() {
        Timber.d(" Updating rating guide ");
        scoreWeightAdapter.notifyDataSetChanged();
    }
}
