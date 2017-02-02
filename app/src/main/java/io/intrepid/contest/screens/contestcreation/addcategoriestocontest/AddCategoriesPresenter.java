package io.intrepid.contest.screens.contestcreation.addcategoriestocontest;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.screens.contestcreation.ValidatableView;
import timber.log.Timber;

import static io.intrepid.contest.screens.contestcreation.addcategoriestocontest.AddCategoriesContract.View;

class AddCategoriesPresenter extends BasePresenter<AddCategoriesContract.View> implements AddCategoriesContract.Presenter {
    private final List<Category> categories = new ArrayList<>();

    AddCategoriesPresenter(@NonNull View view, @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onNextClicked(String name, String description) {
        Timber.d("Adding new category " + name + " " + description);
        view.addCategory(new Category(name, description));
        view.showCategoriesList();
    }
}
