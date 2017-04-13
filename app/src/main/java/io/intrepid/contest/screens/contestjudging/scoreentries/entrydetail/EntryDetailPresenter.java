package io.intrepid.contest.screens.contestjudging.scoreentries.entrydetail;

import android.support.annotation.NonNull;

import java.util.List;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.EntryBallot;

public class EntryDetailPresenter extends BasePresenter<EntryDetailContract.View> implements EntryDetailContract.Presenter {
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
        getView().setNextEnabled(determineNextVisibility());
    }

    private boolean determineNextVisibility() {
        return getView().getEntryToRate().isCompletelyScored();
    }

    private void updateViewWithBallot() {
        entries = getView().getAllEntries();
        getView().showEntries(entries);

        entryBeingRated = getView().getEntryToRate();
        Entry detailEntry = getView().getEntryToRate();
        getView().scrollToEntry(entries.indexOf(detailEntry));
    }

    @Override
    public void onScoreChanged(int position, int newRating) {
        getView().getEntryBallot().setScore(position, newRating);
        entryBeingRated = getView().getEntryToRate();
        entryBeingRated.acceptScore(position, newRating);
        getView().scrollToEntry(entries.indexOf(entryBeingRated));
        if (entries.indexOf(entryBeingRated) == entries.size() - 1) {
            getView().setReviewRatingsButtonVisibility(allBallotsScored());
        }
        getView().setNextEnabled(determineNextVisibility());
    }

    private boolean allBallotsScored() {
        for (EntryBallot ballot : getView().getAllBallots()) {
            if (!ballot.isCompletelyScored()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onEntryScoreReviewClicked() {
        getView().returnToEntriesListPage(true);
    }

    @Override
    public void onPageScrolled() {
        getView().setNextEnabled(determineNextVisibility());
    }

    @Override
    public void onSnapViewSwiped(int newIndex) {
        entryBeingRated = entries.get(newIndex);
        if (getView() != null) {
            getView().onPageSwipedTo(newIndex);
        }
    }
}
