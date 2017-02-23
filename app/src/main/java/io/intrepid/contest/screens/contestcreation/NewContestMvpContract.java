package io.intrepid.contest.screens.contestcreation;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Contest;

class NewContestMvpContract {

    public interface View extends BaseContract.View {

        void cancelEdit();

        void showContestSubmissionPage(int page);

        ContestCreationFragment getChildEditFragment(int pageIndex);

        void setNextVisible(boolean visible);

        int getCurrentIndex();

        void showUpdatedCategories();

        void navigateToAddCategoryPage(Contest.Builder  contest);

        void navigateToSendInvitationsScreen();
    }

    public interface Presenter extends BaseContract.Presenter<View> {

        void onNextButtonClicked();

        void onBackButtonClicked();

        void onNextStatusChanged(boolean nextEnabled);

        void onNewCategoryAdded(String name, String description);

        void showNextScreen();

        void showAddCategoryScreen();
    }
}
