package io.intrepid.contest.screens.contestjudging.scoreentries;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.EntryBallot;
import io.intrepid.contest.screens.contestjudging.EntryOnClickListener;

class ScoresEntriesContract {
    public interface View extends BaseContract.View, EntryOnClickListener {
        void showEntriesList();

        void showEntryDetail(int humanReadableIndex);

        void cancelScoringEntries();
    }

    public interface Presenter extends BaseContract.Presenter<ScoresEntriesContract.View> {
        void onNextClicked();

        void onBackPressed();

        void onEntrySelected(Entry entry);

        List<Category> getCategories();

        Entry getCurrentEntry();

        EntryBallot getCurrentEntryBallot();

        List<Entry> getEntries();

        List<EntryBallot> getEntryBallotsList();
    }
}
