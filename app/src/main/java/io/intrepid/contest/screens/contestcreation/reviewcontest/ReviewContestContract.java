package io.intrepid.contest.screens.contestcreation.reviewcontest;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Contest;

interface ReviewContestContract {

    interface View extends BaseContract.View {
        void displayReviewPageContent(Contest.Builder contest);

        void showEditTitlePage(Contest.Builder contestBuilder);

        void showEditDescriptionPage(Contest.Builder contestBuilder);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onContestTitleSelected();

        void onContestDescriptionSelected();

        void onPageSelected();

        boolean isNextPageButtonEnabled();
    }
}
