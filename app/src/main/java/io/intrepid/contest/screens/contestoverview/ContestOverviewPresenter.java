package io.intrepid.contest.screens.contestoverview;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.models.ScoreWeight;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class ContestOverviewPresenter extends BasePresenter<ContestOverviewContract.View>
        implements ContestOverviewContract.Presenter {

    private static final int TEMPORARY_NUM_SUBMISSIONS_WAITING = 3;
    private static final List<ScoreWeight> scoreWeights = Arrays.asList(new ScoreWeight(1, R.string.poor),
                                                                        new ScoreWeight(2, R.string.average),
                                                                        new ScoreWeight(3, R.string.good),
                                                                        new ScoreWeight(4, R.string.great),
                                                                        new ScoreWeight(5, R.string.excellent));

    ContestOverviewPresenter(@NonNull ContestOverviewContract.View view,
                             @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        requestContestDetails();

        // TODO: this number will come from judge scoring
        view.showNumSubmissionsWaiting(TEMPORARY_NUM_SUBMISSIONS_WAITING);
    }

    private void requestContestDetails() {
        String contestId = persistentSettings.getCurrentContestId().toString();
        Disposable apiCallDisposable = restApi.getContestDetails(contestId)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                    updateViewWithContest(response.contest);
                }, throwable -> {
                    Timber.d("API error retrieving contest details: " + throwable.getMessage());
                    view.showMessage(R.string.error_api);
                });
        disposables.add(apiCallDisposable);
    }

    private void updateViewWithContest(Contest contest) {
        view.showContestName(contest.getTitle());
        view.showContestDescription(contest.getDescription());
        showCategoriesForContest(contest);
    }

    private void showCategoriesForContest(Contest contest) {
        List<Category> categories = contest.getCategories();
        view.showCategoriesAndWeights(categories, scoreWeights);
    }
}
