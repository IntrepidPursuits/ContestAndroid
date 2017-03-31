package io.intrepid.contest.screens.contestresults.shareresults;


import android.support.annotation.NonNull;

import java.util.List;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.RankedEntryResult;

class ShareResultsPresenter extends BasePresenter<ShareResultsContract.View> implements ShareResultsContract.Presenter {

    private final List<RankedEntryResult> topResults;

    ShareResultsPresenter(@NonNull ShareResultsContract.View view,
                          @NonNull PresenterConfiguration configuration,
                          List<RankedEntryResult> topResults) {
        super(view, configuration);
        this.topResults = topResults;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        view.showResultsList(topResults);
    }
}
