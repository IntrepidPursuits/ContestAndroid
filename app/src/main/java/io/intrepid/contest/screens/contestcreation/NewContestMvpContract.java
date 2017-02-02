package io.intrepid.contest.screens.contestcreation;

import android.content.Intent;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;

class NewContestMvpContract {

    public interface View extends BaseContract.View {

        void cancelEdit();

        void initializePages(Contest.Builder contest);

        void showContestSubmissionPage(int page);

        void completeEditForm(Contest contest);

        ContestCreationFragment getChildEditFragment(int pageIndex);

        void setNextVisible(boolean visible);

        int getCurrentIndex();

        void showUpdatedCategories(Contest.Builder contest);
    }

    public interface Presenter extends BaseContract.Presenter<View> {

        void onNextButtonClicked();

        void onBackButtonClicked();

        void setContestName(String contestName);

        void setContestDescription(String contestDescription);

        void setCategories(List<Category> categories);

        void onNextStatusChanged(boolean nextEnabled);

        void addCategory(Category category);

        void onNewCategoryAdded(Intent data);
    }
}
