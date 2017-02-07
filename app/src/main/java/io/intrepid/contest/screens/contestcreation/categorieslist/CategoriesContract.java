package io.intrepid.contest.screens.contestcreation.categorieslist;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;

interface CategoriesContract {

    interface View extends BaseContract.View {
        void showCategories(List<Category> categories);
        void showDefaultCategory();
        void submitCategories(List<Category> categories);
        void showAddCategoryScreen();
        void showPreviewContestPage();
    }

    interface Presenter extends BaseContract.Presenter<CategoriesContract.View> {
        void displayCategories(Contest.Builder contest);
        void onNextClicked(List<Category> categories);
        void onAddCategoryClicked();
    }
}

