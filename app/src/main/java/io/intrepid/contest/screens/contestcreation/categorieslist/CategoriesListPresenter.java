package io.intrepid.contest.screens.contestcreation.categorieslist;

import android.support.annotation.NonNull;

import java.util.List;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;


class CategoriesListPresenter extends BasePresenter<CategoriesContract.View> implements CategoriesContract.Presenter {
    private static final String DEFAULT_CATEGORY_NAME = "Example Category";
    private static final String DEFAULT_CATEGORY_DESCRIPTION = "This is an example";
    private List<Category> categories;

    CategoriesListPresenter(@NonNull CategoriesContract.View view,
                            @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void displayCategories(Contest contest) {
        if (contest.getCategories() == null) {
            injectDefaultCategoryInView();
        }
    }

    private void injectDefaultCategoryInView() {
        categories.clear();
        Category exampleCategory = new Category(DEFAULT_CATEGORY_NAME, DEFAULT_CATEGORY_DESCRIPTION);
        categories.add(exampleCategory);
        view.showCategories(categories);
    }
}
