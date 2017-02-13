package io.intrepid.contest.screens.contestcreation.editcategoriestocontest;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import timber.log.Timber;

import static io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoriesContract.View;

class EditCategoriesPresenter extends BasePresenter<EditCategoriesContract.View> implements EditCategoriesContract.Presenter {
    private final List<Category> categories = new ArrayList<>();

    EditCategoriesPresenter(@NonNull View view, @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onNextClicked(String name, String description) {
        Timber.d("Adding new category " + name + " " + description);
        view.addCategory(new Category(name, description));
        view.showCategoriesList();
    }
}
