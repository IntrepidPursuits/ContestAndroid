package io.intrepid.contest.screens.contestcreation;

import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.List;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;
import timber.log.Timber;

class NewContestPresenter extends BasePresenter<NewContestMvpContract.View> implements NewContestMvpContract.Presenter {
    public Contest.Builder contest;

    NewContestPresenter(@NonNull NewContestMvpContract.View view,
                        @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
        contest = new Contest.Builder();
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        contest = new Contest.Builder();
        view.initializePages(contest);
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

    @Override
    public void setContestName(String contestName) {
        contest.setTitle(contestName);
        view.setNextVisible(false);
        showNextScreen();
    }

    private void showNextScreen() {
        int currentIndex = view.getCurrentIndex();
        view.showContestSubmissionPage(currentIndex + 1);
    }

    @Override
    public void setContestDescription(String contestDescription) {
        contest.setDescription(contestDescription);
        showNextScreen();
    }

    @Override
    public void setCategories(List<Category> categories) {
        contest.categories = categories;
        view.completeEditForm(contest.build());
    }

    @Override
    public void onNextStatusChanged(boolean nextEnabled) {
        view.setNextVisible(nextEnabled);
    }

    @Override
    public void addCategory(Category category) {
        Timber.d("Category added " + category);
        contest.categories.add(category);
        //Navigate Back to List after Adding Category
        onBackButtonClicked();
    }

    @Override
    public void onNewCategoryAdded(Intent data) {
        //todo - Add category
        view.showUpdatedCategories(contest);
    }
}
