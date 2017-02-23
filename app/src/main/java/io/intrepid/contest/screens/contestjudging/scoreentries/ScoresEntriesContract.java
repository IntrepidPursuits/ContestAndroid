package io.intrepid.contest.screens.contestjudging.scoreentries;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.screens.contestjudging.EntryOnClickListener;

class ScoresEntriesContract {
    public interface View extends BaseContract.View, EntryOnClickListener {
        void showEntriesList();

        void setNextEnabled(boolean enabled);

        void showEntryDetail(int humanReadableIndex, int size);

        void cancelScoringEntries();
    }

    public interface Presenter extends BaseContract.Presenter<ScoresEntriesContract.View> {
        void onNextClicked();

        void onBackPressed();

        void onEntryClicked(Entry entry);

        List<Category> getCategories();

        List<Entry> getEntries();

        Entry getCurrentEntry();
    }
}
