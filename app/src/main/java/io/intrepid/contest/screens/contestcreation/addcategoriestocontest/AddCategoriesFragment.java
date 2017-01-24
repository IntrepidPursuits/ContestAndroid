package io.intrepid.contest.screens.contestcreation.addcategoriestocontest;

import io.intrepid.contest.R;
import io.intrepid.contest.screens.contestcreation.BaseNewContestFragment;

public class AddCategoriesFragment extends BaseNewContestFragment {
    @Override
    public boolean canMoveFurther() {
        return false;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_edit_contest_category;
    }
}
