package io.intrepid.contest.screens.contestjudging.scoreentries;

import java.util.List;

import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.EntryBallot;

public interface ScoreEntriesActivityContract {
    List<Category> getCategories();

    Entry getCurrentEntry();

    EntryBallot getCurrentEntryBallot();

    List<Entry> getEntriesList();

    List<EntryBallot> getEntryBallotsList();

    void onEntryDetailFragmentPageChanged(int newPage);

    void setNextEnabled(boolean enabled);
}
