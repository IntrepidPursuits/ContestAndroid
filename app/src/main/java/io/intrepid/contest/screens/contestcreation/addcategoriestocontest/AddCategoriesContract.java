package io.intrepid.contest.screens.contestcreation.addcategoriestocontest;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;

class AddCategoriesContract {

    public interface View extends BaseContract.View {
        void saveCategories(List<Category> categories);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        void onCategoryNameEntered(String newName, String newDescription);
        void onNextClicked();
    }
}
