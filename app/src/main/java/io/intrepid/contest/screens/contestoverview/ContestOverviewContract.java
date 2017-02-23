package io.intrepid.contest.screens.contestoverview;

import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.ScoreWeight;

class ContestOverviewContract {
    interface View extends BaseContract.View {
        void showContestDescription(String description);

        void showTitle(@StringRes int titleResource, String contestName);

        void showCategoriesAndWeights(List<Category> categories, List<ScoreWeight> weights);

        void advanceToJudgingScreen();

        void showSubmissionCountMessage(@StringRes int messageResource, @PluralsRes int plural);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onOverViewSubmitButtonClicked();
    }
}
