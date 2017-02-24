package io.intrepid.contest.screens.contestjudging.scoreentries.entrieslist;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.EntryBallot;

interface EntriesListContract {
    interface View extends BaseContract.View {
        List<Entry> getEntries();

        void showEntriesList();

        void setNextVisible(boolean nextVisible);

        void showSubmitButton(boolean visible);

        List<EntryBallot> getEntryBallots();

        void showContestStatusScreen();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onSubmitButtonClicked();
    }
}

