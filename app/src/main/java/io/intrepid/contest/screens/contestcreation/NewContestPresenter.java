package io.intrepid.contest.screens.contestcreation;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.rest.ContestWrapper;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class NewContestPresenter extends BasePresenter<NewContestMvpContract.View> implements NewContestMvpContract.Presenter, ViewPager.OnPageChangeListener {
    private static final int NAME_CONTEST_PAGE_INDEX = 0;
    private static final int LAST_PAGE_INDEX = 3;
    private Contest.Builder contest;

    NewContestPresenter(@NonNull NewContestMvpContract.View view,
                        @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
        contest = new Contest.Builder();
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        view.showContestSubmissionPage(0);
    }

    @Override
    public void onNextButtonClicked() {
        int currentIndex = view.getCurrentIndex();
        ContestCreationFragment fragment = view.getChildEditFragment(currentIndex);
        fragment.onNextClicked();
    }

    @Override
    public void onBackButtonClicked() {
        int currentIndex = view.getCurrentIndex();
        if (currentIndex == 0) {
            view.cancelEdit();
        } else {
            view.showContestSubmissionPage(currentIndex - 1);
        }
    }

    public Contest.Builder getContest() {
        return contest;
    }

    public void showNextScreen() {
        int currentIndex = view.getCurrentIndex();
        if (currentIndex == LAST_PAGE_INDEX) {
            submitContest();
        }
        view.showContestSubmissionPage(currentIndex + 1);
    }

    @Override
    public void onNextStatusChanged(boolean nextEnabled) {
        view.setNextVisible(nextEnabled);
    }

    @Override
    public void onNewCategoryAdded(String categoryName, String categoryDescription) {
        contest.categories.add(new Category(categoryName, categoryDescription));
        view.showUpdatedCategories();
    }

    @Override
    public void showAddCategoryScreen() {
        view.navigateToAddCategoryPage(contest);
    }

    private void submitContest() {
        view.showMessage(R.string.submitting_contest);
        Disposable submitCall = restApi.submitContest(new ContestWrapper(contest.build()))
                .compose(subscribeOnIoObserveOnUi())
                .subscribe((this::onApiResult), throwable -> {
                    Timber.d("API error creating contest " + throwable.getMessage());
                    view.showMessage(R.string.error_api);
                });
        disposables.add(submitCall);
    }

    private void onApiResult(ContestWrapper response) {
        view.showMessage(response.contest.toString() + " was created ");
        persistentSettings.setCurrentContestId(response.contest.getId());
        view.navigateToSendInvitationsScreen();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        onPageSelected(position);
    }

    @Override
    public void onPageSelected(int position) {
        if (position == NAME_CONTEST_PAGE_INDEX) {
            ValidatableView fragment = (ValidatableView) view.getChildEditFragment(position);
            fragment.onFocus();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        int position = view.getCurrentIndex();
        onPageSelected(position);
    }
}
