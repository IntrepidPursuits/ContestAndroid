package io.intrepid.contest.screens.contestcreation.categorieslist;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;

interface CategoriesContract {

    interface View extends BaseContract.View {
        void showCategories(List<Category> categories);
        void onCategoryClicked(Category category);
        void showCategoryForReview(Category category);
        void showDefaultCategory();
    }

    interface Presenter extends BaseContract.Presenter<CategoriesContract.View> {
        void displayCategories(Contest contest);
        void onCategoryClicked(Category category);
        void onNext();
    }
}
