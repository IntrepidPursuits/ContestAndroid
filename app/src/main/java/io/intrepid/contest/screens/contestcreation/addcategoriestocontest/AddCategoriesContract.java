package io.intrepid.contest.screens.contestcreation.addcategoriestocontest;

import io.intrepid.contest.base.BaseContract;

class AddCategoriesContract {

    public interface View extends BaseContract.View {
        void onCategoryNameEntered(String name);

        void onCategoryDescriptonEntered(String description);
        void onCancelClicked();

        void onEditComplete();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        void submitCategory();

        void editCategoryName(String newName);

        void editCategoryDescription(String newName);
    }
}
