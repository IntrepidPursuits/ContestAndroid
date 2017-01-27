package io.intrepid.contest.screens.contestcreation.addcategoriestocontest;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;

public class AddCategoriesContract {

    public interface View extends BaseContract.View {
        void onCategoryEntered(Category category);
        void onCancelClicked();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        void addCategory(Category category);
    }
}
