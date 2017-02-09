package io.intrepid.contest.screens.contestcreation;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;

class NewContestPresenter extends BasePresenter<NewContestMvpContract.View> implements NewContestMvpContract.Presenter {
    private Contest.Builder contest;

    NewContestPresenter(@NonNull NewContestMvpContract.View view,
                               @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
        contest = new Contest.Builder();
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        contest = new Contest.Builder();
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

    @Override
    public void showNextScreen() {
        int currentIndex = view.getCurrentIndex();
        view.showContestSubmissionPage(currentIndex + 1);
    }

    @Override
    public void onNextStatusChanged(boolean nextEnabled) {
        view.setNextVisible(nextEnabled);
    }

    @Override
    public void onNewCategoryAdded(String categoryName, String categoryDescription) {
        contest.categories.add(new Category(categoryName, categoryDescription));
        view.showUpdatedCategories(contest);
    }

    @Override
    public void showAddCategoryScreen() {
        view.navigateToAddCategoryPage(contest);
    }
}
