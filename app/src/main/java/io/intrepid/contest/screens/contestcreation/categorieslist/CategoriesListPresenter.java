package io.intrepid.contest.screens.contestcreation.categorieslist;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;

class CategoriesListPresenter extends BasePresenter<CategoriesListContract.ContestCreationFragment> implements CategoriesListContract.Presenter {
    private final Contest.Builder contestBuilder;

    CategoriesListPresenter(@NonNull CategoriesListContract.ContestCreationFragment view,
                            @NonNull PresenterConfiguration configuration,
                            Contest.Builder contestBuilder) {
        super(view, configuration);
        this.contestBuilder = contestBuilder;
        List<Category> categories = this.contestBuilder.getCategories();
        categories = categories == null ? new ArrayList<>() : categories;

        if (categories.isEmpty()) {
            Category defaultCategory = view.getDefaultCategory(R.string.default_category_name,
                                                               R.string.default_category_description);
            categories.add(defaultCategory);
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
    public void onNextClicked() {
        view.showNextScreen();
    }

    @Override
    public void onAddCategoryClicked() {
        view.showAddCategoryScreen();
    }

    @Override
    public void onCategoryClicked(Category category) {
        view.showEditCategoryPage(category, contestBuilder.getCategories().indexOf(category));
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

    @Override
    public void onCategoryMoved(int fromPosition, int toPosition) {
        List<Category> categories = contestBuilder.getCategories();
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(categories, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(categories, i, i - 1);
            }
        }
    }
}
