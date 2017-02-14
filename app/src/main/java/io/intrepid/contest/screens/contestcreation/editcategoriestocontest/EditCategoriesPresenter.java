package io.intrepid.contest.screens.contestcreation.editcategoriestocontest;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import timber.log.Timber;

import static io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoriesContract.View;

class EditCategoriesPresenter extends BasePresenter<EditCategoriesContract.View> implements EditCategoriesContract.Presenter {
    private final Category category;
    private boolean editMode = false;

    EditCategoriesPresenter(@NonNull View view, @NonNull PresenterConfiguration configuration, Category category) {
        super(view, configuration);
        editMode = category != null
                && category.getName().length() > 0
                && category.getDescription().length() > 0;
        this.category = category;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        if (editMode) {
            String categoryName = category.getName();
            String categoryDescription = category.getDescription();
            view.showEditableCategory(categoryName, categoryDescription);
        }
    }

    @Override
    public void onNextClicked(String name, String description) {
        Timber.d("Adding new category " + name + " " + description);
        if (editMode) {
            view.editCategory(category, name, description);
        } else {
            view.addCategory(new Category(name, description));
        }
        view.showCategoriesList();
    }
}
