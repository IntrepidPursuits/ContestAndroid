package io.intrepid.contest.screens.contestcreation;

import android.support.annotation.StringRes;
import android.support.v4.view.ViewPager;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Contest;

class NewContestMvpContract {

    public interface View extends BaseContract.View {
        void cancelEdit();

        void showContestSubmissionPage(int page);

        ContestCreationFragment getChildEditFragment(int pageIndex);

        void setNextPageButtonVisible(boolean visible);

        int getCurrentIndex();

        void showUpdatedCategories();

        void navigateToAddCategoryPage(Contest.Builder contest);

        void navigateToSendInvitationsScreen();

        void setPageTitle(@StringRes int pageTitle);

        void hideKeyboard();
    }

    public interface Presenter extends BaseContract.Presenter<View>, ViewPager.OnPageChangeListener {
        void onNextPageButtonClicked();

        void onBackButtonClicked();

        void onNextPageEnabledChanged();

        void onNewCategoryAdded(String name, String description);

        Contest.Builder getContest();

        void showNextScreen();

        void showAddCategoryScreen();

        void onContestEditEntered(int index, String newName, String newDescription);
    }
}
