package io.intrepid.contest.screens.contestcreation.editcategoriestocontest;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;

class EditCategoriesContract {

    public interface View extends BaseContract.View {
        void addCategory(Category category);

        void showCategoriesList();

        void showEditableCategory(String previousCategoryName, String previousCategoryDescription);

        void editCategory(Category category, String name, String description);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        void onNextClicked(String newName, String newDescription);
    }
}
