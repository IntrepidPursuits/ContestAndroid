package io.intrepid.contest.screens.contestcreation.describecontest;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;

public class DescribeContestContract {

    public interface View extends BaseContract.View {
        void onContestDescriptionDone(Category category);
        void onCancelClicked();
    }

    public interface Presenter<View> {
        void submitContestDescription(String descriptionName);
    }
}
