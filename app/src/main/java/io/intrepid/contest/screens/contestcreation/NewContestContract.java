package io.intrepid.contest.screens.contestcreation;

import android.content.Intent;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;

public class NewContestContract {

    public interface View extends BaseContract.View {

        int getNumberOfScreens();

        void acceptContestDescription(String contestDescription);

        void acceptCategory(Category category);

        void onCancelClicked();

        void returnToPreviousScreen();

        void advanceToNextScreen();

        void showNewlyCreatedContest(Contest contest);
    }

    public interface Presenter extends BaseContract.Presenter<View> {

        boolean canAdvanceToNextScreen();

        boolean canReturnToPreviousSceen();

        void setContestName(String name);

        void publishContest(Contest contest);

        void cancelContestCreation();

        void onNextButtonClicked();

        void onBackButtonClicked();

        void initializeContestBuilder(Intent intent);

        void addCategory(Category category);

        void setContestDescription(String contestDescription);
    }
}
