package io.intrepid.contest.screens.contestcreation.categorieslist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.R;
import io.intrepid.contest.models.Category;
import timber.log.Timber;

class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> implements CategoryViewHolder.CategoryClickListener {
    private final Context context;
    private List<Category> categories;
    private Category exampleCategory;

    CategoryAdapter(Context context) {
        this.context = context;
        categories = new ArrayList<>();
        makeExampleCategory();
    }

    private void makeExampleCategory() {
        final String categoryName = context.getString(R.string.category_name_example);
        final String categoryDescription = context.getString(R.string.category_description_example);
        exampleCategory = new Category(categoryName, categoryDescription);
    }

    void setExampleCategories() {
        categories.clear();
        categories.add(exampleCategory);
        notifyDataSetChanged();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(parent, this);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.bindCategory(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public void onCategoryClicked(Category category) {
        Timber.d(category + " was clicked for review/edit");
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            setExampleCategories();
        } else {
            this.categories = categories;
        }
        notifyDataSetChanged();
    }
}
