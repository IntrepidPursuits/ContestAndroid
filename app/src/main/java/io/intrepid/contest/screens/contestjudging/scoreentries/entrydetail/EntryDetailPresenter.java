package io.intrepid.contest.screens.contestjudging.scoreentries.entrydetail;

import android.support.annotation.NonNull;

import java.util.List;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.EntryBallot;

class EntryDetailPresenter extends BasePresenter<EntryDetailContract.View> implements EntryDetailContract.Presenter {
    private Entry entryBeingRated;
    private List<Entry> entries;

    EntryDetailPresenter(@NonNull EntryDetailContract.View view,
                         @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        updateViewWithBallot();
        view.setNextEnabled(determineNextVisibility());
    }

    private boolean determineNextVisibility() {
        return view.getEntryToRate().isCompletelyScored();
    }

    private void updateViewWithBallot() {
        entries = view.getAllEntries();
        view.showEntries(entries);

        entryBeingRated = view.getEntryToRate();
        Entry detailEntry = view.getEntryToRate();
        view.scrollToEntry(entries.indexOf(detailEntry));
    }

    @Override
    public void onScoreChanged(int position, int newRating) {
        view.getEntryBallot().setScore(position, newRating);
        entryBeingRated = view.getEntryToRate();
        entryBeingRated.acceptScore(position, newRating);
        view.scrollToEntry(entries.indexOf(entryBeingRated));
        if (entries.indexOf(entryBeingRated) == entries.size() - 1) {
            view.setReviewRatingsButtonVisibility(allBallotsScored());
        }
        view.setNextEnabled(determineNextVisibility());
    }

    private boolean allBallotsScored() {
        for (EntryBallot ballot : view.getAllBallots()) {
            if (!ballot.isCompletelyScored()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onEntryScoreReviewClicked() {
        view.returnToEntriesListPage(true);
    }

    @Override
    public void onPageScrolled() {
        view.setNextEnabled(determineNextVisibility());
    }

    @Override
    public void onSnapViewSwiped(int newIndex) {
        entryBeingRated = entries.get(newIndex);
        if (view != null) {
            view.onPageSwipedTo(newIndex);
        }
    }
}
