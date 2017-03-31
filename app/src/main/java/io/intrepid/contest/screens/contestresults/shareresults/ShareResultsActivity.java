package io.intrepid.contest.screens.contestresults.shareresults;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.RankedEntryResult;
import io.intrepid.contest.screens.contestresults.ContestResultsAdapter;

public class ShareResultsActivity extends BaseMvpActivity<ShareResultsContract.Presenter> implements ShareResultsContract.View {
    private static final String TOP_RANK_EXTRA = "Top Ranking Extras";
    @BindView(R.id.generic_recycler_view)
    RecyclerView topResultsRecyclerView;
    private ContestResultsAdapter adapter;

    public static Intent makeIntent(Context context, List<RankedEntryResult> topRankSublist) {
        return new Intent(context, ShareResultsActivity.class).putParcelableArrayListExtra(TOP_RANK_EXTRA,
                                                                                           (ArrayList<RankedEntryResult>) topRankSublist);
    }

    @NonNull
    @Override
    public ShareResultsContract.Presenter createPresenter(PresenterConfiguration configuration) {
        List<RankedEntryResult> topResults = getIntent().getParcelableArrayListExtra(TOP_RANK_EXTRA);
        return new ShareResultsPresenter(this, configuration, topResults);
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        setActionBarTitle(R.string.results);
        adapter = new ContestResultsAdapter();
        topResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        topResultsRecyclerView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_share_results;
    }

    @Override
    public void showResultsList(List<RankedEntryResult> topResults) {
        adapter.updateResultsList(topResults);
    }
}
