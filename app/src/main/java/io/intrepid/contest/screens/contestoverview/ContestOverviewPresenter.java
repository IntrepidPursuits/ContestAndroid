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

public class ContestOverviewPresenter extends BasePresenter<ContestOverviewContract.View>
        implements ContestOverviewContract.Presenter {

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
    }

    private void requestContestDetails() {
        String contestId = getPersistentSettings().getCurrentContestId().toString();
        Disposable apiCallDisposable = getRestApi().getContestDetails(contestId)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                    Contest contest = response.contest;
                    updateViewWithContest(contest);
                }, throwable -> {
                    Timber.d("API error retrieving contest details: " + throwable.getMessage());
                    getView().showMessage(R.string.error_api);
                });
        getDisposables().add(apiCallDisposable);
    }

    private void updateViewWithContest(Contest contest) {
        getView().showContestDescription(contest.getDescription());
        showCategoriesForContest(contest);
        getView().showSubmissionCountMessage(contest.getEntries().size(), R.plurals.numberOfSubmissions);
        getView().showTitle(R.string.welcome_to_contest_text, contest.getTitle());
    }

    private void showCategoriesForContest(Contest contest) {
        List<Category> categories = contest.getCategories();
        getView().showCategoriesAndWeights(categories, scoreWeights);
    }

    @Override
    public void onOverViewSubmitButtonClicked() {
        getView().advanceToJudgingScreen();
    }

    @Override
    public void onBackPressed() {
        getView().returnToSplashScreen();
    }
}
