package io.intrepid.contest.screens.contestcreation.categorieslist;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.screens.contestcreation.ValidatableContestCreationFragment;

interface CategoriesListContract {

    interface View extends BaseContract.View, ValidatableContestCreationFragment {
        void showCategories(List<Category> categories);

        void showAddCategoryScreen();

        void showNextScreen();

        void showEditCategoryPage(Category category, int indexOfCategory);

        Category getDefaultCategory(int categoryName, int categoryDescription);

        void onNextPageEnabledChanged(boolean isEnabled);
    }

    interface Presenter extends BaseContract.Presenter<View>, CategoryClickListener {
        void onNextPageButtonClicked();

        void onAddCategoryClicked();

        boolean isNextPageButtonEnabled();
    }
}
