package io.intrepid.contest.screens.contestjudging.scoreentries;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.EntryBallot;
import io.intrepid.contest.models.Score;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class ScoreEntriesPresenter extends BasePresenter<ScoresEntriesContract.View> implements ScoresEntriesContract.Presenter {
    private final List<EntryBallot> entryBallots = new ArrayList<>();
    private List<Entry> entries;
    private List<Category> categories;
    private Entry currentEntry;
    private EntryBallot currentEntryBallot;

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
        getView().showEntriesList();
    }

    private void fetchEntriesForContest() {
        String contestId = getPersistentSettings().getCurrentContestId().toString();
        Disposable apiCallDisposable = getRestApi().getContestDetails(contestId)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                    Timber.d("Received response " + response.contest.getTitle());
                    this.entries = response.contest.getEntries();
                    this.categories = response.contest.getCategories();
                    entryBallots.clear();
                    for (Entry entry : entries) {
                        entry.setCategoriesSize(categories.size());
                        EntryBallot ballot = new EntryBallot(entry.id);
                        for(Category category : categories){
                            ballot.addScore(new Score(category, 0));
                        }
                        entryBallots.add(ballot);
                    }
                    Timber.d("Entry Ballots size " + entryBallots.size());
                    getView().showEntriesList();
                }, throwable -> {
                    Timber.d("API error retrieving contest details: " + throwable.getMessage());
                    getView().showMessage(R.string.error_api);
                });
        getDisposables().add(apiCallDisposable);
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
    public EntryBallot getCurrentEntryBallot() {
        return currentEntryBallot;
    }

    @Override
    public List<Entry> getEntries() {
        return entries;
    }

    @Override
    public List<EntryBallot> getEntryBallotsList() {
        return entryBallots;
    }

    @Override
    public void onNextClicked() {
        if (currentEntry == null) {
            return;
        }

        int index = entries.indexOf(currentEntry);
        if (index == entries.size() - 1) {
            showEntriesList();
        } else {
            index++;
            currentEntry = entries.get(index);
            currentEntryBallot = entryBallots.get(index);
            getView().showEntryDetail(index);
        }
    }

    @Override
    public void onBackPressed() {
        if (currentEntry != null) {
            showEntriesList();
        } else {
            getView().cancelScoringEntries();
        }
    }

    @Override
    public void onEntrySelected(Entry entry) {
        currentEntry = entry;
        currentEntryBallot = entryBallots.get(entries.indexOf(entry));
        getView().showEntryDetail(entries.indexOf(entry));
    }
}
