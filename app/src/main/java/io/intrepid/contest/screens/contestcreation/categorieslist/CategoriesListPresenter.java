package io.intrepid.contest.screens.contestcreation.categorieslist;

import android.support.annotation.NonNull;

import java.util.List;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;


class CategoriesListPresenter extends BasePresenter<CategoriesContract.View> implements CategoriesContract.Presenter {

    CategoriesListPresenter(@NonNull CategoriesContract.View view,
                            @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void displayCategories(Contest contest) {
        if (contest == null || contest.getCategories().size() == 0) {
            view.showDefaultCategory();
        }else{
            view.showCategories(contest.getCategories());
        }
    }

    @Override
    public void onNextClicked(List<Category> categories) {
        view.submitCategories(categories);
    }
}
