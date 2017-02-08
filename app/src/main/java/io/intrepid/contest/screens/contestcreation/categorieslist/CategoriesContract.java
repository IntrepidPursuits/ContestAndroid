package io.intrepid.contest.screens.contestcreation.categorieslist;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;

interface CategoriesContract {

    interface View extends BaseContract.View {
        void showCategories(List<Category> categories);
        void showDefaultCategory();
        void showAddCategoryScreen();

        void showNextScreen();
    }

    interface Presenter extends BaseContract.Presenter<CategoriesContract.View> {
        void displayCategories();
        void onNextClicked(List<Category> categories);
        void onAddCategoryClicked();
    }
}

