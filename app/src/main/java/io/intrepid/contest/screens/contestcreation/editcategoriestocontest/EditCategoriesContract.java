package io.intrepid.contest.screens.contestcreation.editcategoriestocontest;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;

class EditCategoriesContract {

    public interface View extends BaseContract.View {
        void addCategory(Category category);

        void showCategoriesList();

        void showEditableCategory(String previousCategoryName, String previousCategoryDescription);

        void editCategory(int index, String name, String description);

        void setNextVisible(boolean b);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        void onNextClicked(String newName, String newDescription);

        void onCategoryNameChanged(CharSequence newName);
    }
}
