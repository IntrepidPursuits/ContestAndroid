package io.intrepid.contest.screens.contestresults;

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
import io.intrepid.contest.models.RankedEntryResult;
import io.intrepid.contest.screens.splash.SplashActivity;

import static android.view.View.GONE;

public class ContestResultsActivity extends BaseMvpActivity<ContestResultsContract.Presenter>
        implements ContestResultsContract.View {

    @BindView(R.id.generic_recycler_view)
    RecyclerView contestResultsRecyclerView;
    @BindView(R.id.contest_results_no_entries_text_view)
    TextView noEntriesTextView;

    private ContestResultsAdapter contestResultsAdapter;

    public static Intent makeIntent(Context context) {
        return new Intent(context, ContestResultsActivity.class);
    }

    @NonNull
    @Override
    public ContestResultsContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return new ContestResultsPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_contest_results;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        setActionBarTitle(R.string.results);
        setActionBarDisplayHomeAsUpEnabled(true);
        contestResultsAdapter = new ContestResultsAdapter();
        contestResultsRecyclerView.setAdapter(contestResultsAdapter);
        contestResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBackPressed() {
        startActivity(SplashActivity.makeIntent(this));
    }

    @Override
    public void showResults(List<RankedEntryResult> entryResults) {
        contestResultsAdapter.updateResultsList(entryResults);
    }

    @Override
    public void hideNoEntriesMessage() {
        noEntriesTextView.setVisibility(GONE);
    }
}
