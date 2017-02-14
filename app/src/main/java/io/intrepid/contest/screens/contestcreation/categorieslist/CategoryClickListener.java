package io.intrepid.contest.screens.contestcreation.categorieslist;

import io.intrepid.contest.models.Category;

public interface CategoryClickListener {
    void onCategoryClicked(Category category);

    void onDeleteClicked(Category category);
}
