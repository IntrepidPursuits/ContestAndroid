package io.intrepid.contest.screens.contestcreation;

import android.support.annotation.NonNull;

import java.util.List;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;

class NewContestPresenter extends BasePresenter<NewContestMvpContract.View> implements NewContestMvpContract.Presenter {
    public Contest.Builder contest;
    private int screenIndex = 0;

    NewContestPresenter(@NonNull NewContestMvpContract.View view,
                               @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        contest = new Contest.Builder();
        view.showContestSubmissionPage(screenIndex);
    }

    @Override
    public void onNextButtonClicked() {
        ContestCreationFragment childFragment = view.getChildEditFragment(screenIndex);
        if (childFragment != null) {
            childFragment.onNextClicked();
        }
    }

    @Override
    public void onBackButtonClicked() {
        if (screenIndex == 0) {
            view.cancelEdit();
        } else {
            view.showContestSubmissionPage(--screenIndex);
        }
    }

    @Override
    public void setContestName(String contestName) {
        contest.setTitle(contestName);
        view.showContestSubmissionPage(++screenIndex);
    }

    @Override
    public void setContestDescription(String contestDescription) {
        contest.setDescription(contestDescription);
        view.showContestSubmissionPage(++screenIndex);
    }

    @Override
    public void setCategories(List<Category> categories) {
        contest.categories = categories;
        view.completeEditForm(contest.build());
    }
}
