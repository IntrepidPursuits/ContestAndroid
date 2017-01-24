package io.intrepid.contest.screens.contestcreation.reviewcontest;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import io.intrepid.contest.R;
import io.intrepid.contest.screens.contestcreation.BaseNewContestFragment;

public class CategoriesListFragment extends BaseNewContestFragment {

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem nextButton = menu.findItem(R.id.action_next);
        nextButton.setIcon( android.R.drawable.ic_media_next);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_contest_categories_rv;
    }

    @Override
    public boolean canMoveFurther() {
        return true;
    }

    @Override
    public int cantMoveFurtherErrorMessage() {
        return R.string.error_msg;
    }
}
