package io.intrepid.contest.screens.contestcreation.editcategoriestocontest;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;

class EditCategoriesContract {

    public interface View extends BaseContract.View {
        void addCategory(Category category);
        void showCategoriesList();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        void onNextClicked(String newName, String newDescription);
    }
}
