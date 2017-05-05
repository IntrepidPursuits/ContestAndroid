package io.intrepid.contest.screens.contestcreation;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.VisibleForTesting;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.rest.ContestWrapper;
import io.intrepid.contest.screens.contestcreation.reviewcontest.ReviewContestFragment;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class NewContestPresenter extends BasePresenter<NewContestMvpContract.View>
        implements NewContestMvpContract.Presenter {
    @VisibleForTesting
    static final int LAST_PAGE_INDEX = 3;
    @VisibleForTesting
    Contest.Builder contest;

    NewContestPresenter(@NonNull NewContestMvpContract.View view,
                        @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
        contest = new Contest.Builder();
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getView().showContestSubmissionPage(0);
    }

    @Override
    protected void onViewBound() {
        super.onViewBound();
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
    }

    @Override
    public void onNextPageButtonClicked() {
        ContestCreationFragment fragment = getCurrentPageFragment();
        fragment.onNextPageButtonClicked();
    }

    @Override
    public void onBackButtonClicked() {
        int currentIndex = getView().getCurrentIndex();
        if (currentIndex == 0) {
            getView().cancelEdit();
        } else {
            getView().showContestSubmissionPage(currentIndex - 1);
        }
    }

    @Override
    public Contest.Builder getContest() {
        return contest;
    }

    @Override
    public void showNextScreen() {
        int currentIndex = getView().getCurrentIndex();
        if (currentIndex == LAST_PAGE_INDEX) {
            submitContest();
        }
        getView().showContestSubmissionPage(currentIndex + 1);
    }

    @Override
    public void onNextPageEnabledChanged() {
        updateNextPageButtonVisibility();
    }

    @Override
    public void onNewCategoryAdded(String categoryName, String categoryDescription) {
        contest.getCategories().add(new Category(categoryName, categoryDescription));
        getView().showUpdatedCategories();
    }

    @Override
    public void showAddCategoryScreen() {
        getView().navigateToAddCategoryPage(contest);
    }

    @Override
    public void onContestEditEntered(int index, String newName, String newDescription) {
        Category category = contest.getCategories().get(index);
        if (category != null) {
            category.setName(newName);
            category.setDescription(newDescription);
            getView().showUpdatedCategories();
        }
    }

    private void submitContest() {
        getView().showMessage(R.string.submitting_contest);
        Disposable submitCall = getRestApi().submitContest(new ContestWrapper(contest.build()))
                .compose(subscribeOnIoObserveOnUi())
                .subscribe((this::onApiResult), throwable -> {
                    Timber.d("API error creating contest " + throwable.getMessage());
                    getView().showMessage(R.string.error_api);
                });
        getDisposables().add(submitCall);
    }

    private void onApiResult(ContestWrapper response) {
        Timber.d("Contest id " + response.contest.getId());
        getPersistentSettings().setCurrentContestId(response.contest.getId());
        getView().navigateToSendInvitationsScreen();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Do nothing
    }

    @Override
    public void onPageSelected(int position) {
        ContestCreationFragment childEditFragment = getView().getChildEditFragment(position);
        if (childEditFragment instanceof ValidatableContestCreationFragment) {
            ValidatableContestCreationFragment fragment = (ValidatableContestCreationFragment) childEditFragment;
            fragment.onFocus();
        }

        @StringRes int pageTitle;
        switch (position) {
            case 1:
                pageTitle = R.string.description;
                break;
            case 2:
                pageTitle = R.string.scoring_categories;
                break;
            case 3:
                pageTitle = R.string.review_contest;
                break;
            default:
                pageTitle = R.string.new_contest;
        }
        getView().setPageTitle(pageTitle);

        if (childEditFragment instanceof ReviewContestFragment) {
            ((ReviewContestFragment) childEditFragment).onPageSelected();
        }

        updateNextPageButtonVisibility();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // Do nothing
    }

    private ContestCreationFragment getCurrentPageFragment() {
        int currentIndex = getView().getCurrentIndex();
        return getView().getChildEditFragment(currentIndex);
    }

    private void updateNextPageButtonVisibility() {
        getView().setNextPageButtonVisible(getCurrentPageFragment().isNextPageButtonEnabled());
    }
}
