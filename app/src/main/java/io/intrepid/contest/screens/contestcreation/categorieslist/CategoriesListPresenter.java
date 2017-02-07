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
    public void displayCategories(Contest.Builder contest) {
        if (contest == null || contest.categories.size() == 0) {
            view.showDefaultCategory();
        } else {
            view.showCategories(contest.categories);
        }
    }

    @Override
    public void onNextClicked(List<Category> categories) {
        view.showPreviewContestPage();
    }

    @Override
    public void onAddCategoryClicked() {
        view.showAddCategoryScreen();
    }
}
