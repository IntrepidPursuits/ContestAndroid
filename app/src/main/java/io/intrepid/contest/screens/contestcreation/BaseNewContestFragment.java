package io.intrepid.contest.screens.contestcreation;

import butterknife.Unbinder;
import io.intrepid.contest.BaseSlideFragment;
import io.intrepid.contest.R;


public abstract class BaseNewContestFragment extends BaseSlideFragment{

    @Override
    public boolean canMoveFurther() {
        return false;
    }

    @Override
    public int cantMoveFurtherErrorMessage() {
        return R.string.error_msg;
    }

}

