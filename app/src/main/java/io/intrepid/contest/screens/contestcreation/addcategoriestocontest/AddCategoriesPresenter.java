package io.intrepid.contest.screens.contestcreation.addcategoriestocontest;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;

import static io.intrepid.contest.screens.contestcreation.addcategoriestocontest.AddCategoriesContract.*;
import static io.intrepid.contest.screens.contestcreation.addcategoriestocontest.AddCategoriesContract.View;


public class AddCategoriesPresenter extends BasePresenter<AddCategoriesContract.View> implements AddCategoriesContract.Presenter{

    public AddCategoriesPresenter(@NonNull View view, @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void addCategory(Category category) {

    }
}
