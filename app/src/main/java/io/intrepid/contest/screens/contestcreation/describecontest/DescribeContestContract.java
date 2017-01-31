package io.intrepid.contest.screens.contestcreation.describecontest;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;

class DescribeContestContract {

    public interface View extends BaseContract.View {
        void saveContestDescription(String description);
        void showError();
    }

    public interface Presenter<View> {
        void onNextClicked(String description);
    }
}
