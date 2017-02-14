package io.intrepid.contest.screens.contestcreation.categorieslist;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.screens.contestcreation.ValidatableView;

interface CategoriesListContract {

    interface View extends BaseContract.View, ValidatableView {
        void showCategories(List<Category> categories);

        void showAddCategoryScreen();

        void showNextScreen();

        void showEditCategoryPage(Category category);

        Category getDefaultCategory(int categoryName, int categoryDescription);

        void setNextEnabled(boolean enabled);
    }

    interface Presenter extends BaseContract.Presenter<CategoriesListContract.View>, CategoryClickListener {
        void displayCategories();

        void onNextClicked(List<Category> categories);

        void onAddCategoryClicked();
    }
}
