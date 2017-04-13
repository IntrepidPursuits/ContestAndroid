package io.intrepid.contest.screens.contestcreation.categorieslist;

import io.intrepid.contest.models.Category;

interface CategoryClickListener{
    void onCategoryClicked(Category category);

    void onDeleteClicked(Category category);

    void onCategoryMoved(int fromPosition, int toPosition);
}
