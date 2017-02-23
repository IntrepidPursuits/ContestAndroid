package io.intrepid.contest.screens.contestjudging.scoreentries.entrydetail;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.Score;
import io.intrepid.contest.screens.contestjudging.expandablerecycler.CategoryScoreListener;

class EntryDetailContract {
    public interface View extends BaseContract.View {
        Entry getEntryToRate();

        void showEntry(Entry entry);

        void showListOfEntryScores(List<Score> entryScores);

        List<Category> getCategories();

        void setNextEnabled(boolean nextEnabled);
    }

    public interface Presenter extends BaseContract.Presenter<View>, CategoryScoreListener {
    }
}
