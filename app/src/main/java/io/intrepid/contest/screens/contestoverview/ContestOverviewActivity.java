package io.intrepid.contest.screens.contestoverview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.ScoreWeight;
import io.intrepid.contest.screens.contestjudging.scoreentries.ScoreEntriesActivity;
import io.intrepid.contest.screens.splash.SplashActivity;
import io.intrepid.contest.utils.SpannableUtil;

public class ContestOverviewActivity extends BaseMvpActivity<ContestOverviewContract.Presenter, ContestOverviewContract.View>
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getPresenter().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        getPresenter().onBackPressed();
    }

    @OnClick(R.id.contest_overview_submit_button)
    public void onOverviewSubmitButtonClicked() {
        getPresenter().onOverViewSubmitButtonClicked();
    }

    private void setupCategoriesRecyclerView() {
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new DualCategoryScoreAdapter();
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public void showContestDescription(String description) {
        descriptionTextView.setText(description);
    }

    @Override
    public void showTitle(@StringRes int titleResource, String contestName) {
        String welcomeText = getString(titleResource, contestName);
        setActionBarTitle(welcomeText);
    }

    @Override
    public void showCategoriesAndWeights(List<Category> categories, List<ScoreWeight> weights) {
        categoryAdapter.setData(categories, weights);
    }

    @Override
    public void advanceToJudgingScreen() {
        startActivity(ScoreEntriesActivity.makeIntent(this));
    }

    @Override
    public void showSubmissionCountMessage(int submissionCount, @PluralsRes int plural) {
        String quantifiedText = getResources().getQuantityString(plural, submissionCount, submissionCount);
        SpannableString fullText = SpannableString.valueOf(getString(R.string.contest_overview_intro, quantifiedText));
        String numericalTextInString = quantifiedText.split(" ")[0];
        SpannableUtil.getInstance().setColor(this, fullText, numericalTextInString, R.color.colorPrimary);
        SpannableUtil.getInstance().setBold(fullText, numericalTextInString);
        introTextView.setText(fullText);
    }

    @Override
    public void returnToSplashScreen() {
        startActivity(SplashActivity.makeIntent(this));
    }
}
