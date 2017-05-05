package io.intrepid.contest.screens.contestresults;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.RankedEntryResult;
import io.intrepid.contest.screens.contestresults.shareresults.ShareResultsActivity;
import io.intrepid.contest.screens.entrysubmission.cropimage.CustomUCrop;
import io.intrepid.contest.screens.splash.SplashActivity;
import io.intrepid.contest.utils.BitmapToUriUtil;
import io.intrepid.contest.utils.ScreenshotHelperUtil;
import timber.log.Timber;

import static android.view.View.GONE;

public class ContestResultsActivity extends BaseMvpActivity<ContestResultsContract.Presenter, ContestResultsContract.View>
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_result_contest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share: presenter.onShareActionClicked();
        }
        return true;
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

    @Override
    public void showTopRankPreview(List<RankedEntryResult> topRankSublist) {
        startActivity(ShareResultsActivity.makeIntent(this, topRankSublist));
    }
}