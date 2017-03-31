package io.intrepid.contest.screens.contestresults.shareresults;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.RankedEntryResult;
import io.intrepid.contest.screens.contestresults.ContestResultsAdapter;
import io.intrepid.contest.utils.BitmapToUriUtil;
import io.intrepid.contest.utils.ScreenshotHelperUtil;
import timber.log.Timber;

public class ShareResultsActivity extends BaseMvpActivity<ShareResultsContract.Presenter> implements ShareResultsContract.View {
    private static final String TOP_RANK_EXTRA = "Top Ranking Extras";
    private static final String SHARE_CONTENT_EXTRA_KEY = "ShareContent Extra Key";
    @BindView(R.id.generic_recycler_view)
    RecyclerView topResultsRecyclerView;
    private ContestResultsAdapter adapter;
    private ShareActionProvider shareActionProvider;
    private Uri sharableUri;

    public static Intent makeIntent(Context context, List<RankedEntryResult> topRankSublist) {
        return new Intent(context, ShareResultsActivity.class).putParcelableArrayListExtra(
                TOP_RANK_EXTRA,
                (ArrayList<RankedEntryResult>) topRankSublist);
    }

    @NonNull
    @Override
    public ShareResultsContract.Presenter createPresenter(PresenterConfiguration configuration) {
        List<RankedEntryResult> topResults = getIntent().getParcelableArrayListExtra(TOP_RANK_EXTRA);
        return new ShareResultsPresenter(this, configuration, topResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share_results, menu);
        MenuItem actionItem = menu.findItem(R.id.action_share_results);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(actionItem);
        if (shareActionProvider == null) {
            return false;
        }
        shareActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        setShareIntent();
        actionItem.setIcon(android.R.drawable.ic_menu_share);
        shareActionProvider.setOnShareTargetSelectedListener((source, intent) -> {
            intent.putExtra(Intent.EXTRA_SUBJECT, "Check out th");
            return false;
        });
        return true;
    }

    private void setShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        Timber.d("Sharable uri " + sharableUri);
        shareIntent.putExtra(SHARE_CONTENT_EXTRA_KEY, sharableUri);

        shareIntent.putExtra(Intent.EXTRA_TEXT, "Checkout contest results " + sharableUri);
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public void setShareOptions(Uri uri) {
        sharableUri = uri;
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_results:
                presenter.onSaveResultsClicked();
                break;
            case R.id.action_share_results:
                presenter.onShareResultsClicked();
                break;
        }
        return true;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        setActionBarTitle(R.string.results);
        adapter = new ContestResultsAdapter();
        topResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        topResultsRecyclerView.setAdapter(adapter);
        supportInvalidateOptionsMenu();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.include_layout_recycler_view;
    }

    @Override
    public void showResultsList(List<RankedEntryResult> topResults) {
        adapter.updateResultsList(topResults);
    }

    @Override
    public Uri captureScreenshot() {
        Bitmap bitmap = ScreenshotHelperUtil.getScreenshotFromRV(topResultsRecyclerView);
        sharableUri = BitmapToUriUtil.convertToContentUri(this, bitmap);
        Timber.d("Sharable uri " + sharableUri);
        return sharableUri;
    }

    @Override
    public Uri saveInGallery(Bitmap bitmap) {
        return BitmapToUriUtil.convertToFileUri(this, bitmap);
    }
}