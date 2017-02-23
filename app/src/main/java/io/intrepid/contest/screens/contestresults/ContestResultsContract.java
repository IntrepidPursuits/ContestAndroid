package io.intrepid.contest.screens.contestresults;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.RankedEntryResult;

class ContestResultsContract {
    interface View extends BaseContract.View {
        void showResults(List<RankedEntryResult> entryResults);

        void hideNoEntriesMessage();
    }

    interface Presenter extends BaseContract.Presenter<View> {

    }
}
