package io.intrepid.contest.screens.contestjudging.scoreentries;

import android.support.annotation.NonNull;

import java.util.List;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Entry;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class ScoreEntriesPresenter extends BasePresenter<ScoresEntriesContract.View> implements ScoresEntriesContract.Presenter {
    private List<Entry> entries;
    private List<Category> categories;
    private Entry currentEntry;

    ScoreEntriesPresenter(@NonNull ScoresEntriesContract.View view,
                          @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        fetchEntriesForContest();
    }

    private void showEntriesList() {
        currentEntry = null;
        view.showEntriesList();
    }

    private void fetchEntriesForContest() {
        String contestId = persistentSettings.getCurrentContestId().toString();
        Disposable apiCallDisposable = restApi.getContestDetails(contestId)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                    Timber.d("Received response " + response.contest.getTitle());
                    this.entries = response.contest.getEntries();
                    this.categories = response.contest.getCategories();
                    view.showEntriesList();
                }, throwable -> {
                    Timber.d("API error retrieving contest details: " + throwable.getMessage());
                    view.showMessage(R.string.error_api);
                });
        disposables.add(apiCallDisposable);
    }

    @Override
    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public Entry getCurrentEntry() {
        return currentEntry;
    }

    @Override
    public List<Entry> getEntries() {
        return entries;
    }

    @Override
    public void onNextClicked() {
        if (currentEntry != null) {
            int index = entries.indexOf(currentEntry);
            if (index < entries.size() - 1) {
                currentEntry = entries.get(++index);
                int humanReadableIndex = index + 1;
                view.showEntryDetail(humanReadableIndex, entries.size());
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (currentEntry != null) {
            showEntriesList();
        } else {
            view.cancelScoringEntries();
        }
    }

    @Override
    public void onEntryClicked(Entry entry) {
        currentEntry = entry;
        int humanReadableIndex = entries.indexOf(entry) + 1;
        view.showEntryDetail(humanReadableIndex, entries.size());
    }
}