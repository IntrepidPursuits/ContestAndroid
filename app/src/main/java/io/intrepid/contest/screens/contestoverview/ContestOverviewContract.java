package io.intrepid.contest.screens.contestoverview;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.ScoreWeight;

class ContestOverviewContract {
    interface View extends BaseContract.View {
        void showContestName(String contestName);

        void showContestDescription(String description);

        void showCategoriesAndWeights(List<Category> categories, List<ScoreWeight> weights);

        void showNumSubmissionsWaiting(int numSubmissionsWaiting);
    }

    interface Presenter extends BaseContract.Presenter<View> {

    }
}
