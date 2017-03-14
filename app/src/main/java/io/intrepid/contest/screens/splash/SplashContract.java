package io.intrepid.contest.screens.splash;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Contest;

interface SplashContract {

    interface View extends BaseContract.View {
        void showCreateContestScreen();

        void showJoinContestScreen();

        void showUserButtons();

        void showOngoingContests(List<Contest> contests);

        void resumeContest(Contest contest);
    }

    interface Presenter extends BaseContract.Presenter<View>, ContestRowViewHolder.OnContestClickedListener {
        void onCreateContestClicked();

        void onJoinContestClicked();
    }
}
