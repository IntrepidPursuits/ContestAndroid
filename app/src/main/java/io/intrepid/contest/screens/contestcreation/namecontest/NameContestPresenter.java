package io.intrepid.contest.screens.contestcreation.namecontest;

import io.intrepid.contest.base.BaseContract;

public class NameContestPresenter {

    public interface View extends BaseContract.View{
        void acceptContestName(String contestName);
    }

    public interface Presenter {

    }
}
