package io.intrepid.contest.screens.contestoverview;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;

class ContestOverviewContract {
    interface View extends BaseContract.View {
        void showContestName(String contestName);

        void showContestDescription(String description);

        void showCategories(List<Category> categories);

        void showNumSubmissionsWaiting(int numSubmissionsWaiting);

        void showRatingGuide();
    }

    interface Presenter extends BaseContract.Presenter<View> {

    }
}
