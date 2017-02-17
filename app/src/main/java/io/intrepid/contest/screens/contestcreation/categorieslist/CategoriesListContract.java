package io.intrepid.contest.screens.contestcreation.categorieslist;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;

interface CategoriesListContract {

    interface View extends BaseContract.View {
        void showCategories(List<Category> categories);

        void showAddCategoryScreen();

        void showNextScreen();

        void showEditCategoryPage(Category category);

        Category getDefaultCategory(int categoryName, int categoryDescription);
    }

    interface Presenter extends BaseContract.Presenter<CategoriesListContract.View>, CategoryClickListener {
        void displayCategories();

        void onNextClicked(List<Category> categories);

        void onAddCategoryClicked();
    }
}
