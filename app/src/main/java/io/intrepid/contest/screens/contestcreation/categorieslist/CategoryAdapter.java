package io.intrepid.contest.screens.contestcreation.categorieslist;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.intrepid.contest.models.Category;
import io.intrepid.contest.utils.dragdrop.ItemTouchHelperAdapter;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> implements ItemTouchHelperAdapter {
    private final List<Category> categories = new ArrayList<>();
    @LayoutRes
    private int rowItemLayout;
    private CategoryClickListener listener;

    public CategoryAdapter(@LayoutRes int rowLayout, CategoryClickListener listener) {
        rowItemLayout = rowLayout;
        this.listener = listener;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(parent, listener, rowItemLayout);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.bindCategory(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
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
