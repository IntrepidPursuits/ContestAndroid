package io.intrepid.contest.screens.contestjudging.scoreentries.entrydetail;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.EntryBallot;
import io.intrepid.contest.screens.contestjudging.expandablerecycler.CategoryScoreListener;
import io.intrepid.contest.utils.CustomSnapHelper;

class EntryDetailContract {
    public interface View extends BaseContract.View {
        Entry getEntryToRate();

        void setReviewRatingsButtonVisibility(boolean visible);

        List<Category> getCategories();

        void setNextEnabled(boolean nextEnabled);

        EntryBallot getEntryBallot();

        List<EntryBallot> getAllBallots();

        void returnToEntriesListPage(boolean review);

        List<Entry> getAllEntries();

        void showEntries(List<Entry> entries);

        void onPageSwipedTo(int pageIndex);

        void scrollToEntry(int pageIndex);
    }

    public interface Presenter extends BaseContract.Presenter<View>, CategoryScoreListener, CustomSnapHelper.OnPageSwipeListener {
        void onEntryScoreReviewClicked();

        void onPageScrolled();
    }
}
