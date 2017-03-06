package io.intrepid.contest.screens.adminstatus;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Entry;

class AdminStatusContract {

    interface View extends BaseContract.View {

        void showAwaitSubmissionsIndicator(boolean inProgress);

        void showJudgingStatusIndicator(boolean inProgress);

        void showEndOfContestIndicator(boolean inProgress);

        void showSubmittedEntries(List<Entry> entries);

        void showConfirmStartContestDialog();

        void showConfirmEndContestDialog();

        void advanceToJudgingIndicator();

        void exitStatusScreen();
    }

    interface Presenter extends BaseContract.Presenter<View> {

        void onBottomNavigationButtonClicked();

        void onBackPressed();
    }
}
