package io.intrepid.contest.screens.contestcreation.namecontest;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Contest;

public class NameContestContract {

    public interface View extends BaseContract.View {
        void saveEnteredName(String contestName);
        void showError();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        void onContestNameUpdate(String contestName);
    }
}
