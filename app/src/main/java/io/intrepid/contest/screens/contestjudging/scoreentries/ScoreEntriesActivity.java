package io.intrepid.contest.screens.contestjudging.scoreentries;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.EntryBallot;
import io.intrepid.contest.screens.contestjudging.scoreentries.entrieslist.EntriesListFragment;
import io.intrepid.contest.screens.contestjudging.scoreentries.entrydetail.EntryDetailFragment;

public class ScoreEntriesActivity extends BaseMvpActivity<ScoreEntriesPresenter>
        implements ScoresEntriesContract.View, ScoreEntriesActivityContract {

    private boolean nextVisible = false;

    public static Intent makeIntent(Context context) {
        return new Intent(context, ScoreEntriesActivity.class);
    }

    @NonNull
    @Override
    public ScoreEntriesPresenter createPresenter(PresenterConfiguration configuration) {
        return new ScoreEntriesPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_fragment_container;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        setActionBarTitle(R.string.submissions);
        setActionBarDisplayHomeAsUpEnabled(true);
    }

    private void replaceFragment(Fragment fragment) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment == fragment) {
            return;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_new_contest, menu);
        MenuItem nextItem = menu.findItem(R.id.action_next);
        nextItem.setVisible(nextVisible);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_next:
                presenter.onNextClicked();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }

    @Override
    public void showEntriesList() {
        replaceFragment(new EntriesListFragment());
    }

    @Override
    public void showEntryDetail(int pageIndex) {
        updateToolbarTitleForIndex(pageIndex);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof EntryDetailFragment) {
            ((EntryDetailFragment) fragment).scrollToEntry(pageIndex);
        } else {
            replaceFragment(new EntryDetailFragment());
        }
    }

    private void updateToolbarTitleForIndex(int index) {
        int totalPages = getEntriesList().size();
        int humanReadableIndex = index + 1;
        setActionBarTitle(getString(R.string.rate_entry_x_out_of_entries, humanReadableIndex, totalPages));
    }

    @Override
    public void cancelScoringEntries() {
        super.onBackPressed();
    }

    @Override
    public void setNextEnabled(boolean enabled) {
        nextVisible = enabled;
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onEntryClicked(Entry entry) {
        presenter.onEntrySelected(entry);
    }

    @Override
    public List<Category> getCategories() {
        return presenter.getCategories();
    }

    @Override
    public Entry getCurrentEntry() {
        return presenter.getCurrentEntry();
    }

    @Override
    public EntryBallot getCurrentEntryBallot() {
        return presenter.getCurrentEntryBallot();
    }

    @Override
    public List<Entry> getEntriesList() {
        return presenter.getEntries();
    }

    @Override
    public List<EntryBallot> getEntryBallotsList() {
        return presenter.getEntryBallotsList();
    }

    @Override
    public void onEntryDetailFragmentPageChanged(int newPage) {
        presenter.onEntrySelected(getEntriesList().get(newPage));
    }
}
