package io.intrepid.contest.screens.contestcreation.namecontest;

import io.intrepid.contest.base.BaseContract;

public class NameContestContract {

    public interface View extends BaseContract.View {
        void acceptContestDescription(String contestDescription);
    }

    public interface Presenter extends BaseContract.Presenter<View> {

    }
}
