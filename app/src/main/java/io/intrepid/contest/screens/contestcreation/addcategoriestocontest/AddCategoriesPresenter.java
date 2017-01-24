package io.intrepid.contest.screens.contestcreation.addcategoriestocontest;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.screens.contestcreation.ValidatableView;

import static io.intrepid.contest.screens.contestcreation.addcategoriestocontest.AddCategoriesContract.View;

class AddCategoriesPresenter extends BasePresenter<AddCategoriesContract.View> implements AddCategoriesContract.Presenter {
    private List<Category> categories = new ArrayList<>();

    AddCategoriesPresenter(@NonNull View view, @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onCategoryNameEntered(String newName, String newDescription) {
        if(newName == null || newDescription == null){
            ((ValidatableView) view).showError();
        }else {
            categories.add(new Category(newName, newDescription));
        }
    }

    @Override
    public void onNextClicked() {
        view.saveCategories(categories);
    }
}
