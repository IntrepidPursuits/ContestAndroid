package io.intrepid.contest.screens.contestresults.shareresults;


import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.RankedEntryResult;

public class ShareResultsContract {
    public interface View extends BaseContract.View {

        void showResultsList(List<RankedEntryResult> topResults);
    }
    public interface Presenter extends BaseContract.Presenter<View> {
    }
}
