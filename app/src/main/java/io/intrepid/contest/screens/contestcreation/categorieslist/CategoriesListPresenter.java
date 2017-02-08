package io.intrepid.contest.screens.contestcreation.categorieslist;

import android.support.annotation.NonNull;

import java.util.List;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;


class CategoriesListPresenter extends BasePresenter<CategoriesListContract.View> implements CategoriesListContract.Presenter {

    private final Contest.Builder contestBuilder;

    CategoriesListPresenter(@NonNull CategoriesListContract.View view,
                            @NonNull PresenterConfiguration configuration,
                            Contest.Builder contestBuilder) {
        super(view, configuration);
        this.contestBuilder = contestBuilder;
    }

    @Override
    public void displayCategories() {
        if (contestBuilder == null || contestBuilder.getCategories().size() == 0) {
            view.showDefaultCategory();
        } else {
            view.showCategories(contestBuilder.getCategories());
        }
    }

    @Override
    public void onNextClicked(List<Category> categories) {
        contestBuilder.setCategories(categories);
        view.showNextScreen();
    }

    @Override
    public void onAddCategoryClicked() {
        view.showAddCategoryScreen();
    }

    @Override
    public void onCategoryClicked(Category category) {
        view.showEditCategoryPage(category);
    }
}
