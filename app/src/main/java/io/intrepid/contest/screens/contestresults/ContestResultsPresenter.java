package io.intrepid.contest.screens.contestresults;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.RankedEntryResult;
import io.intrepid.contest.rest.ContestResultResponse;
import io.intrepid.contest.utils.BitmapToUriUtil;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class ContestResultsPresenter extends BasePresenter<ContestResultsContract.View>
        implements ContestResultsContract.Presenter {

    private List<RankedEntryResult> entryResults;

    public ContestResultsPresenter(@NonNull ContestResultsContract.View view,
                                   @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();

        String contestId = getPersistentSettings().getCurrentContestId().toString();
        Disposable apiCallDisposable = getRestApi().getContestResults(contestId)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(contestResultResponse -> showResults(contestResultResponse), throwable -> {
                    Timber.d("API error retrieving contest results: " + throwable.getMessage());
                    getView().showMessage(R.string.error_api);
                });
        getDisposables().add(apiCallDisposable);
    }

    private void showResults(ContestResultResponse contestResultResponse) {
        if (contestResultResponse.contestResults == null) {
            return;
        }

        entryResults = contestResultResponse.contestResults.rankedScoredEntries;

        if (!entryResults.isEmpty()) {
            getView().hideNoEntriesMessage();
            getView().showResults(entryResults);
        }
    }

    @Override
    public void onShareActionClicked() {
        List<RankedEntryResult> topRankSublist = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            topRankSublist.add(entryResults.get(i));
        }
        view.showTopRankPreview(topRankSublist);
    }
}
