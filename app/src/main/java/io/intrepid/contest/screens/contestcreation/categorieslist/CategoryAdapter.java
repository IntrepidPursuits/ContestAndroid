package io.intrepid.contest.screens.contestcreation.categorieslist;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.intrepid.contest.R;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.utils.dragdrop.ItemTouchHelperAdapter;
import timber.log.Timber;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> implements ItemTouchHelperAdapter, CategoryViewHolder.CategoryClickListener {
    private final Context context;
    private List<Category> categories;
    private Category exampleCategory;
    @LayoutRes
    private int rowItemLayout;

    public CategoryAdapter(Context context, @LayoutRes int rowLayout) {
        this.context = context;
        rowItemLayout = rowLayout;
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
        return new CategoryViewHolder(parent, this, rowItemLayout);
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

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(categories, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(categories, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }
}
