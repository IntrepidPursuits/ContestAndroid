package io.intrepid.contest.screens.contestcreation.categorieslist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.models.Category;

class CategoryViewHolder extends RecyclerView.ViewHolder {
    private final CategoryClickListener listener;
    private Category category;

    @BindView(R.id.category_item_title)
    TextView categoryTitle;
    @BindView(R.id.category_item_description)
    TextView categoryDescription;

    CategoryViewHolder(ViewGroup parent, CategoryClickListener listener) {
        super(inflateView(parent));
        this.listener = listener;
        ButterKnife.bind(this, itemView);
    }

    private static View inflateView(ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return layoutInflater.inflate(R.layout.category_row_item, parent, false);
    }

    void bindCategory(Category category) {
        this.category = category;
        categoryTitle.setText(category.getName());
        categoryDescription.setText(category.getDescription());
    }

    @OnClick(R.id.scoring_category_card_view)
    void onCategoryClicked(){
        listener.onCategoryClicked(category);
    }

    interface CategoryClickListener{
        void onCategoryClicked(Category category);
    }
}
