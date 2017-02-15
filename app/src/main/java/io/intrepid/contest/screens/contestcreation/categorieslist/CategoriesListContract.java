package io.intrepid.contest.screens.contestcreation.categorieslist;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;

interface CategoriesListContract {

    interface View extends BaseContract.View {
        void showCategories(List<Category> categories);
        void showDefaultCategory();
        void showAddCategoryScreen();
        void showNextScreen();
        void showEditCategoryPage(Category category);
        void onCategoryClicked(Category category);
    }

    interface Presenter extends BaseContract.Presenter<CategoriesListContract.View> {
        void displayCategories();
        void onNextClicked(List<Category> categories);
        void onAddCategoryClicked();
        void onCategoryClicked(Category category);
    }
}

