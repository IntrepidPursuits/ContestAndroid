package io.intrepid.contest.screens.contestcreation.editcategoriestocontest;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import timber.log.Timber;

import static io.intrepid.contest.screens.contestcreation.editcategoriestocontest.EditCategoriesContract.View;

public class EditCategoriesPresenter extends BasePresenter<EditCategoriesContract.View> implements EditCategoriesContract.Presenter {
    private final int index;
    private final Category category;
    private boolean editMode = false;
    private boolean nextPageButtonEnabled = false;

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
            getView().showEditableCategory(categoryName, categoryDescription);
            updateNextPageButtonEnabled(true);
        }
    }

    @Override
    public void onNextPageButtonClicked(String name, String description) {
        Timber.d("Adding new category " + name + " " + description);
        if (editMode) {
            getView().editCategory(index, name, description);
        } else {
            getView().addCategory(new Category(name, description));
        }
        getView().showCategoriesList();
    }

    @Override
    public void onCategoryNameChanged(CharSequence newName) {
        updateNextPageButtonEnabled(!newName.toString().isEmpty());
    }

    private void updateNextPageButtonEnabled(boolean isEnabled) {
        if (isEnabled != nextPageButtonEnabled) {
            nextPageButtonEnabled = isEnabled;
            getView().onNextPageEnabledChanged();
        }
    }

    @Override
    public boolean isNextPageButtonEnabled() {
        return nextPageButtonEnabled;
    }
}
