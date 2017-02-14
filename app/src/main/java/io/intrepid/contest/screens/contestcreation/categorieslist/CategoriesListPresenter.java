package io.intrepid.contest.screens.contestcreation.categorieslist;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.R;
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
        List<Category> categories = this.contestBuilder.categories;
        categories = categories == null ? new ArrayList<>() : categories;

        if (categories.isEmpty()) {
            Category defaultCategory = view.getDefaultCategory(R.string.default_category_name,
                                                               R.string.default_category_description);
            categories.add(defaultCategory);
            this.contestBuilder.setCategories(categories);
        }
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        displayCategories();
    }

    @Override
    protected void onViewBound() {
        super.onViewBound();
        determineNextIconVisibility();
    }

    @Override
    public void displayCategories() {
        view.showCategories(contestBuilder.getCategories());
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

    @Override
    public void onDeleteClicked(Category category) {
        contestBuilder.getCategories().remove(category);
        view.showCategories(contestBuilder.getCategories());
        determineNextIconVisibility();
    }

    private void determineNextIconVisibility() {
        boolean nextEnabled = !contestBuilder.getCategories().isEmpty();
        view.setNextEnabled(nextEnabled);
    }
}
