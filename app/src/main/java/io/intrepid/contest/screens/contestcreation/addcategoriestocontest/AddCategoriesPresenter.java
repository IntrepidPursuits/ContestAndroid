package io.intrepid.contest.screens.contestcreation.addcategoriestocontest;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;

import static io.intrepid.contest.screens.contestcreation.addcategoriestocontest.AddCategoriesContract.View;

class AddCategoriesPresenter extends BasePresenter<AddCategoriesContract.View> implements AddCategoriesContract.Presenter {

    AddCategoriesPresenter(@NonNull View view, @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onNextClicked(String name, String description) {
        view.addCategory(new Category(name, description));
        view.showCategoriesList();
    }
}
