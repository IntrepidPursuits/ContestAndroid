package io.intrepid.contest.screens.contestcreation;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.base.TextValidatableView;
import io.intrepid.contest.models.Contest;

class NewContestContract {

    public interface View extends BaseContract.View {

        void onBackNavigationPressed();

        void onForwardNavigationPressed();

        void showContestSubmissionPage(int page);

        void cancelEdit();

        void onContestEditComplete();

        TextValidatableView getChildEditFragment(int pageIndex);

        void showNewlyAddedConest(Contest contestSubmission);
    }

    public interface Presenter extends BaseContract.Presenter<View> {

        void onNextButtonClicked();

        void onBackButtonClicked();

        void saveContest();
    }
}
