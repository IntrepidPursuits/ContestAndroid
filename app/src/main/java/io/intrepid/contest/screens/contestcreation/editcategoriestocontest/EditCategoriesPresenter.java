package io.intrepid.contest.screens.contestcreation.editcategoriestocontest;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import timber.log.Timber;

import static io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoriesContract.View;

class EditCategoriesPresenter extends BasePresenter<EditCategoriesContract.View> implements EditCategoriesContract.Presenter {
    private final int index;
    private final Category category;
    private boolean editMode = false;

    EditCategoriesPresenter(@NonNull View view, @NonNull PresenterConfiguration configuration) {
        //Null params and -1 Index to Denote we are creating a new Category
        this(view, configuration, null, -1);
    }

    EditCategoriesPresenter(@NonNull View view,
                            @NonNull PresenterConfiguration configuration,
                            Category category,
                            int index) {
        super(view, configuration);
        this.category = category;
        this.index = index;
        editMode = index >= 0;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        if (editMode) {
            String categoryName = category.getName();
            String categoryDescription = category.getDescription();
            view.showEditableCategory(categoryName, categoryDescription);
        } else {
            view.setNextVisible(false);
        }
    }

    @Override
    public void onNextClicked(String name, String description) {
        Timber.d("Adding new category " + name + " " + description);
        if (editMode) {
            view.editCategory(index, name, description);
        } else {
            view.addCategory(new Category(name, description));
        }
        view.showCategoriesList();
    }

    @Override
    public void onCategoryNameChanged(CharSequence newName) {
        boolean nextVisible = !newName.toString().isEmpty();
        view.setNextVisible(nextVisible);
    }
}
