package io.intrepid.contest.screens.contestcreation.addcategoriestocontest;

import android.support.annotation.NonNull;

import io.intrepid.contest.BaseSlideFragment;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;

public class AddCategoriesFragment extends BaseSlideFragment<AddCategoriesPresenter> implements AddCategoriesContract.View{

    @Override
    public boolean canMoveFurther() {
        return false;
    }

    @Override
    public int cantMoveFurtherErrorMessage() {
        return R.string.error_msg;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_edit_contest_category;
    }

    @NonNull
    @Override
    public AddCategoriesPresenter createPresenter(PresenterConfiguration configuration) {
        return null;
    }

    @Override
    public void onCategoryEntered(Category category) {

    }

    @Override
    public void onCancelClicked() {

    }
}
