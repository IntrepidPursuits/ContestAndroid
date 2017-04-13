package io.intrepid.contest.screens.contestcreation.categorieslist;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.screens.contestcreation.ValidatableContestCreationFragment;

interface CategoriesListContract {

    interface ContestCreationFragment extends BaseContract.View, ValidatableContestCreationFragment {
        void showCategories(List<Category> categories);

        void showAddCategoryScreen();

        void showNextScreen();

        void showEditCategoryPage(Category category, int indexOfCategory);

        Category getDefaultCategory(int categoryName, int categoryDescription);

        void setNextEnabled(boolean enabled);
    }

    interface Presenter extends BaseContract.Presenter<ContestCreationFragment>, CategoryClickListener {
        void displayCategories();

        void onNextClicked();

        void onAddCategoryClicked();
    }
}
