package io.intrepid.contest.screens.contestcreation.reviewcontest;

import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import io.intrepid.contest.BaseSlideFragment;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

public class CategoriesListFragment extends BaseSlideFragment<ReviewContestPresenter> implements ReviewContestContract.View{

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

    @NonNull
    @Override
    public ReviewContestPresenter createPresenter(PresenterConfiguration configuration) {
        return new ReviewContestPresenter(this, configuration);
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
