package io.intrepid.contest.screens.contestcreation.categorieslist;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;

interface CategoriesListContract {

    interface View extends BaseContract.View {
        void showCategories(List<Category> categories);

        void showAddCategoryScreen();

        void showNextScreen();

        void showEditCategoryPage(Category category, int indexOfCategory);

        Category getDefaultCategory(int categoryName, int categoryDescription);

        void setNextEnabled(boolean enabled);

        void onFocus();
    }

    interface Presenter extends BaseContract.Presenter<View>, CategoryClickListener {
        void displayCategories();

        void onNextClicked();

        void onAddCategoryClicked();
    }
}
