package io.intrepid.contest.screens.contestjudging.scoreentries.entrydetail;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.List;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.EntryBallot;
import io.intrepid.contest.models.Score;
import timber.log.Timber;

class EntryDetailPresenter extends BasePresenter<EntryDetailContract.View> implements EntryDetailContract.Presenter {
    @VisibleForTesting
    EntryBallot currentEntryBallot;
    private Entry entryBeingRated;

    EntryDetailPresenter(@NonNull EntryDetailContract.View view,
                         @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        loadCurrentEntry();
        loadEditableEntryScores();
        view.setNextEnabled(determineNextVisibility());
    }

    private boolean determineNextVisibility() {
        return currentEntryBallot.isCompletelyScored();
    }

    private void loadCurrentEntry() {
        entryBeingRated = view.getEntryToRate();

        currentEntryBallot = view.getEntryBallot();

        if (entryBeingRated != null) {
            Timber.d(entryBeingRated.toString());
            view.showEntry(entryBeingRated);
        }
    }

    private void loadEditableEntryScores() {
        List<Category> categories = view.getCategories();

        if (currentEntryBallot.getScores().isEmpty()) {
            for (Category category : categories) {
                Score score = new Score(category, 0);
                currentEntryBallot.addScore(score);
            }
        }
        view.showListOfEntryScores(currentEntryBallot.getScores());
    }

    @Override
    public void onScoreChanged(int position, int newRating) {
        currentEntryBallot.setScore(position, newRating);
        entryBeingRated.acceptScore(position, newRating);
        if (currentEntryBallot.isCompletelyScored()) {
            view.setNextEnabled(true);
        }
    }
}
