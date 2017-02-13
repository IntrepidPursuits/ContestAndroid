package io.intrepid.contest.screens.contestcreation.categorieslist;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import io.intrepid.contest.R;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.utils.dragdrop.ItemTouchHelperViewHolder;

class CategoryViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
    private final CategoryClickListener listener;
    @BindView(R.id.category_item_title)
    TextView categoryTitle;
    @BindView(R.id.category_item_description)
    TextView categoryDescription;
    private Category category;

    CategoryViewHolder(ViewGroup parent, CategoryClickListener listener, @LayoutRes int rowLayout) {
        super(inflateView(parent, rowLayout));
        this.listener = listener;
        ButterKnife.bind(this, itemView);
    }

    private static View inflateView(ViewGroup parent, @LayoutRes int rowItemLayout) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return layoutInflater.inflate(rowItemLayout, parent, false);
    }

    void bindCategory(Category category) {
        this.category = category;
        categoryTitle.setText(category.getName());
        categoryDescription.setText(category.getDescription());
    }

    @Optional
    @OnClick(R.id.scoring_category_card_view)
    void onCategoryClicked() {
        listener.onCategoryClicked(category);
    }

    @Override
    public void onItemSelected() {
        //Do nothing
    }

    @Override
    public void onItemClear() {
        //Do nothing
    }

    interface CategoryClickListener {
        void onCategoryClicked(Category category);
    }
}
